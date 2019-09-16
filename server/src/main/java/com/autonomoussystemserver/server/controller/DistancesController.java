package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DistanceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.Distances;
import com.autonomoussystemserver.server.database.model.InteractionTimes;
import com.autonomoussystemserver.server.database.model.Personality;
import com.autonomoussystemserver.server.database.repository.DevicesRepository;
import com.autonomoussystemserver.server.database.repository.DistancesRepository;
import com.autonomoussystemserver.server.database.repository.InteractionTimesRepository;
import com.autonomoussystemserver.server.database.repository.PersonalityRepository;

import javafx.embed.swing.JFXPanel;

import javax.sound.sampled.*;
import javax.swing.*;

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.lang.management.PlatformLoggingMXBean;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static sun.audio.AudioPlayer.player;

// GET --> POST
@RestController
public class DistancesController {

    @Autowired
    private DistancesRepository distancesRepository;

    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private PersonalityRepository personalityRepository;

    @Autowired
    private InteractionTimesRepository interactionTimesRepository;

    // we pass command line arguments to spring server (in order to set up philips hue bridge
    @Value("${ipAddress}")
    private String ipAddress;

    @Value("${username}")
    private String username;

    private long initialMillisec = System.currentTimeMillis();

    @GetMapping("/distances")
    public org.springframework.data.domain.Page<Distances> getDistances(Pageable pageable) {
        System.out.println("------------------------------------------------------------");
        System.out.println("DistanceController -> GET getDistances");
        return distancesRepository.findAll(pageable);
    }

    @PostMapping("/distances")
    public Distances postDistance(@RequestBody DistanceDto distanceDto) {
        System.out.println("------------------------------------------------------------");
        System.out.println("DistanceController -> POST postDistance");
        // if the distances between two objects exists, delete this row, and then post a new distance value
        // if the values of FROM or TO (i.e the objects are do not exists in database), do not do POST request
        distancesRepository.delete(distanceDto.getFromDevice(), distanceDto.getToDevice());
        distancesRepository.delete(distanceDto.getToDevice(), distanceDto.getFromDevice());

        Devices fromDevice = new Devices();
        Devices toDevice = new Devices();

        fromDevice.setDeviceId(distanceDto.getFromDevice());
        toDevice.setDeviceId(distanceDto.getToDevice());

        Distances distances = new Distances();
        distances.setFromDevice(fromDevice);
        distances.setToDevice(toDevice);
        distances.setDistance(distanceDto.getDistance());

        // Proxemics Theory

        distancesRepository.save(distances);
        System.out.println("DistanceController -> POST distances: " + distances);
        System.out.println("Hue distances.getDistance(): " + distances.getDistance());

        // TODO: We find by IpAddress, but from where we get this IpAddress? get IP from  https://discovery.meethue.com
//        Hue hueData = hueRepository.findByIpAddress("192.168.0.100");
//        hueData.setIpAddress(hueData.getIpAddress());
//        hueData.setUserName(hueData.getUserName());
//        System.out.println("Backend: " + "Hue hueData.getIpAddress(): " + hueData.getIpAddress() + "; hueData.getUserName(): " + hueData.getUserName());
//        HueRepository hueRepository = new HueRepository(hueData.getIpAddress(), hueData.getUserName());
//         HueRepository hueRepository = new HueRepository("192.168.0.100", "vY5t4oArH-K0BUA7430cb1rJ8mC1DYMzkmBWRr91");

        // we need to find Philips Hue in our network
        // we get ipAddress and username of hue Lamp from command line
        // TODO describe from where we get username
        // we get Ip address from the website https://discovery.meethue.com/
        HueRepository hueRepository = new HueRepository(ipAddress, username);
        System.out.println("DistanceController hueRepository, [" + hueRepository + "]; [" + ipAddress + "]; [" + username + "]");

        Devices devNameFrom = devicesRepository.findById(distanceDto.getFromDevice()).orElse(null);
        Devices devNameTo = devicesRepository.findById(distanceDto.getToDevice()).orElse(null);

        System.out.println("devNameFrom.getDeviceType() = " + devNameFrom.getDeviceType());
        System.out.println("devNameTo.getDeviceType() = " + devNameTo.getDeviceType());

        String personalityNameofDev = devNameFrom.getDevicePersonality().getPersonality_name();
        Personality personality = personalityRepository.findByPersonalityName(personalityNameofDev);

        if (devNameTo.getDeviceType().equals("Lamp")) {
            if (distances.getDistance() >= 120 && distances.getDistance() <= 370) { //
                System.out.println("DistanceController Personality personality = " + personality + "; personalityNameofDev = " + personalityNameofDev);
                int brightness = personality.getBri();
                int hue = personality.getHue();
                int saturation = personality.getSat();
                hueRepository.updateBrightness(true, brightness, hue, saturation);
                System.out.println("Hue hueRepository.updateBrightness() brightness = [" + brightness + "]; hue = [" + hue + "]; saturation = [" + saturation + "]");
            } else {
                // else if the mascot is outside of range, then turn off the lamp, or let other mascot to change its state
                hueRepository.updateBrightness(false, 0, 0, 0);
            }

            // here instead of If devTo is Speaker, we will set a timer and say, if 3 minutes is up
        }

        // todo: maybe you don't need to specify that the device is speaker, the music will play every n minutes
        //  regardless of the type of device
        if (distances.getDistance() >= 370) {
            System.out.println("DistanceController Speakers");

            // when there are several mascots with the same maximum interactionTimes value, then we just choose the first row (first Mascot)
            List<Devices> interactionTimes = interactionTimesRepository.findMaximum(PageRequest.of(0, 1));
            // winner is the most active Mascot that the DB returns
            Integer winner = interactionTimes.get(0).getDeviceId();
            System.out.println("DistanceController mostActiveMascot = " + winner);

            // here we get the personality of the mascot that has ID winner
            Devices act = Objects.requireNonNull(devicesRepository.findById(winner).orElse(null));
            String personalityNameOfMascot = act.getDevicePersonality().getPersonality_name();
            Personality personalityOfActiveMascot = personalityRepository.findByPersonalityName(personalityNameOfMascot);

            // TODO: when the distance is more than 370 cm, play a music according to the personality of winner
            String musicGenre = personalityOfActiveMascot.getMusic_genre();
            System.out.println("DistanceController the most active mascot is = " + winner +
                    "; its personality is = " + personalityNameOfMascot + "; and correlated music genre is = " + musicGenre);
            // TODO: Here api for music genre
            // have 3-4 music from each genre, then play sequentially. Locally save these songs
//                Media hit = new Media(new File("src/main/java/com/autonomoussystemserver/server/assets/" + musicGenre + ".mp3").toURI().toString());
//                MediaPlayer mediaPlayer = new MediaPlayer(hit);
//                mediaPlayer.play();

            String songFilePath = "src/main/java/com/autonomoussystemserver/server/assets/" + musicGenre + ".mp3";
            try {
                InputStream input = new FileInputStream(songFilePath);
                AudioStream stream = new AudioStream(input);
                AudioData data = stream.getData();
                ContinuousAudioDataStream loop = new ContinuousAudioDataStream(data);
                player.start(loop);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }


        // TODO: For Mascot-Tablet interaction
        // Tablet may periodically ask, is there any changes in its state. For example, make GET request to server every half of second
        // and ask "do I need to change the color", "do I need to change the color"... It will revoke the information from server about itself
        // Write another app for Tablet in order to change the color of screen
        // There will be retrofit (http client that makes requests to server, and asks what it needs to display at this moment

        return distances;
    }

    public void playMusic() {
        long currentMillisec = System.currentTimeMillis();
        System.out.println(currentMillisec);
        if (currentMillisec - initialMillisec == 60000) {

        }
    }

}

/*
12235	"b0702880-a295-a8ab-f734-031a98a512de"	"Lamp"	[null]
13956	"c08b6bb5-40b7-d552-1db6-a8822ec11ed9"	"Sq"	13886
12958	"88cf77ce-bc91-241a-b8eb-4d041f74acdf"	"Pixel One"	13887

17403	16	13956	12958
17404	20	13956	12235
13955	129	12958	12235

13888	254	57805	"pink"	"rock"	"Extroversion"	198	"green"	4
13889	254	47110	"blue"	"rock"	"Agreeableness"	253	"green"	1
13890	254	12828	"yellow"	"rock"	"Neuroticism"	52	"green"	3
13886	254	3488	"orange"	"rock"	"Openness"	220	"green"	6
13887	254	49460	"violet"	"rock"	"Conscientiousness"	150	"green"	2
 */