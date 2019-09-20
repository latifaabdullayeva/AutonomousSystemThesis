package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DistanceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.Distances;
import com.autonomoussystemserver.server.database.model.Personality;
import com.autonomoussystemserver.server.database.repository.DevicesRepository;
import com.autonomoussystemserver.server.database.repository.DistancesRepository;
import com.autonomoussystemserver.server.database.repository.InteractionTimesRepository;
import com.autonomoussystemserver.server.database.repository.PersonalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;
import uk.co.caprica.vlcj.binding.LibC.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;

import static java.lang.System.exit;

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
    private int counter = 1;
    private MediaPlayerFactory factory;
    private MediaPlayer audioPlayer;
    private Semaphore sync = new Semaphore(0);

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

        distancesRepository.save(distances);
//        System.out.println("DistanceController -> POST distances: " + distances);
//        System.out.println("Hue distances.getDistance(): " + distances.getDistance());

        // this method checks if the time is 3 minutes, play a music
        checkTheTime();

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
//        System.out.println("DistanceController hueRepository, [" + hueRepository + "]; [" + ipAddress + "]; [" + username + "]");

        Devices devNameFrom = devicesRepository.findById(distanceDto.getFromDevice()).orElse(null);
        Devices devNameTo = devicesRepository.findById(distanceDto.getToDevice()).orElse(null);

//        System.out.println("devNameFrom.getDeviceType() = " + devNameFrom.getDeviceType());
//        System.out.println("devNameTo.getDeviceType() = " + devNameTo.getDeviceType());

        String personalityNameofDev = devNameFrom.getDevicePersonality().getPersonality_name();
        Personality personality = personalityRepository.findByPersonalityName(personalityNameofDev);

        if (devNameTo.getDeviceType().equals("Lamp")) {
            if (distances.getDistance() >= 120 && distances.getDistance() <= 370) { //
//                System.out.println("DistanceController Personality personality = " + personality + "; personalityNameofDev = " + personalityNameofDev);
                int brightness = personality.getBri();
                int hue = personality.getHue();
                int saturation = personality.getSat();
                hueRepository.updateBrightness(true, brightness, hue, saturation);
//                System.out.println("Hue hueRepository.updateBrightness() brightness = [" + brightness + "]; hue = [" + hue + "]; saturation = [" + saturation + "]");
            } else {
                // else if the mascot is outside of range, then turn off the lamp, or let other mascot to change its state
                hueRepository.updateBrightness(false, 0, 0, 0);
            }

            // here instead of If devTo is Speaker, we will set a timer and say, if 3 minutes is up
        }

        // TODO: For Mascot-Tablet interaction
        // Tablet may periodically ask, is there any changes in its state. For example, make GET request to server every half of second
        // and ask "do I need to change the color", "do I need to change the color"... It will revoke the information from server about itself
        // Write another app for Tablet in order to change the color of screen
        // There will be retrofit (http client that makes requests to server, and asks what it needs to display at this moment

        return distances;
    }

    // this method periodically checks if the 1 minute (60 000 milisec) passed, if yes, it calls the playMusic method
    private void checkTheTime() {
        // the number of interaction of each Mascot will be checked only after 3 minutes
        // the initial time is the time when server get the first "post distance" request,
        long currentMillisec = System.currentTimeMillis();
        System.out.println(initialMillisec + " = initialMillisec; " + currentMillisec + " = currentMillisec"
                + "; counter = " + counter + "; minus = " + (currentMillisec - initialMillisec));
        // here you specify the time, every 55000-600000 milliseconds (55 sec - 60 sec), the music will play
        // TODO: do not forget to make it 3 minutes (= 180 000 milliseconds)
        if ((currentMillisec - initialMillisec) >= 55000 * counter && (currentMillisec - initialMillisec) <= 60000 * counter) {
            counter += 1;
            chooseWinnerMascot();
        }
    }

    // this method gets from DB the most Active mascot, finds its personality from other table and calls  playMusic method
    private void chooseWinnerMascot() {
        System.out.println("DistanceController Speakers");
        // when there are several mascots with the same maximum interactionTimes value, then we just choose the first row (first Mascot)
        List<Devices> interactionTimes = interactionTimesRepository.findMaximumInteractedMascot(PageRequest.of(0, 1));
        // winner is the most active Mascot that the DB returns
        Integer winnerMascot = interactionTimes.get(0).getDeviceId();
        System.out.println("DistanceController mostActiveMascot = " + winnerMascot);

        // here we get the personality of the mascot that has ID winner
        Devices mostActive = Objects.requireNonNull(devicesRepository.findById(winnerMascot).orElse(null));
        String personalityNameOfMascot = mostActive.getDevicePersonality().getPersonality_name();
        Personality personalityOfActiveMascot = personalityRepository.findByPersonalityName(personalityNameOfMascot);

        // we get the musicGenre of personality of the winnerMascot
        String musicGenre = personalityOfActiveMascot.getMusic_genre();
        System.out.println("DistanceController the most active mascot is = " + winnerMascot +
                "; its personality is = " + personalityNameOfMascot + "; and correlated music genre is = " + musicGenre);

        playMusic(musicGenre + ".mp3");
    }

    private void playMusic(String trackName) {
        try {
            // TODO: cross platform, call Example class from another project
            Process process = new ProcessBuilder()
                    .command("afplay", "/Users/latifaabdullayeva/Desktop/vlc-example/src/main/resources/" + trackName)
                    .start();

            process.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        // TODO: maybe need some logic to stop the music
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