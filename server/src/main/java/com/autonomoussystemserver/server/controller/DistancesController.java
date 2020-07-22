package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DistanceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.Distances;
import com.autonomoussystemserver.server.database.model.Personality;
import com.autonomoussystemserver.server.database.repository.DevicesRepository;
import com.autonomoussystemserver.server.database.repository.DistancesRepository;
import com.autonomoussystemserver.server.database.repository.PersonalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// GET --> POST
@RestController
public class DistancesController {

    @Autowired
    private DistancesRepository distancesRepository;

    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private PersonalityRepository personalityRepository;

    // we pass command line arguments to spring server (in order to set up philips hue bridge and song path)
    @Value("${hueIPAddress}")
    private String hueIPAddress;

    @Value("${hueUsername}")
    private String hueUsername;

    @Value("${musicFolderPath}")
    private String musicFolderPath;

    private List<String> processBuilderList;

    private int trackNumber = 0;

    private static int lastFrom = -1;
    private static int lastTo = -1;

    @GetMapping("/distances")
    public org.springframework.data.domain.Page<Distances> getDistances(Pageable pageable) {
        return distancesRepository.findAll(pageable);
    }

    @PostMapping("/distances")
    public Distances postDistance(@RequestBody DistanceDto distanceDto) {
        System.out.println("DistancesController: postDistance() from DTO: \n" + distanceDto.getFromDevice() + "; "
                + distanceDto.getToDevice() + "; " + distanceDto.getDistance());

        Distances distances = synchronizedPost(distanceDto);

        // we need to find Philips Hue in our network, we get ipAddress and username of hue Lamp from command line
        HueRepository hueRepository = new HueRepository(hueIPAddress, hueUsername);

        Devices devNameFrom = devicesRepository.findById(distanceDto.getFromDevice()).orElse(null);
        Devices devNameTo = devicesRepository.findById(distanceDto.getToDevice()).orElse(null);

        String personalityNameofDev = null;
        if (devNameFrom != null) {
            personalityNameofDev = devNameFrom.getDevicePersonality().getPersonality_name();
        }
        Personality personality = personalityRepository.findByPersonalityName(personalityNameofDev);

        if (devNameTo != null && devNameTo.getDeviceType().equals("Lamp")) {
            if (distances.getDistance() <= 45) {
                int brightness = personality.getBri();
                int hue = personality.getHue();
                int saturation = personality.getSat();
                hueRepository.updateBrightness(true, brightness, hue, saturation);

            } else {
                // else if the mascot is outside of range, then turn off the lamp, or let other mascot to change its state
                hueRepository.updateBrightness(false, 0, 0, 0);
            }
        } else if (devNameTo != null && devNameTo.getDeviceType().equals("Speakers")) {
            String musicGenre = personality.getMusic_genre();

            if (lastFrom != distances.getFromDevice().getDeviceId() &&
                    lastTo != distances.getToDevice().getDeviceId() &&
                    distances.getDistance() <= 45) {
                lastFrom = distances.getFromDevice().getDeviceId();
                lastTo = distances.getToDevice().getDeviceId();

                playMusic(musicGenre);
            }

            if (lastFrom == distances.getFromDevice().getDeviceId() &&
                    lastTo == distances.getToDevice().getDeviceId() &&
                    distances.getDistance() > 45) {

                lastFrom = -1;
                lastTo = -1;

                ProcessManager.clear();
            }

        }

        return distances;
    }

    synchronized private Distances synchronizedPost(DistanceDto distanceDto) {
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

        System.out.println("DistancesController: distances = \n" +
                distances.getFromDevice().getDeviceId() + "; " + distances.getFromDevice().getDeviceType() + "; "
                + distances.getToDevice().getDeviceId() + "; " + distances.getToDevice().getDeviceType() + "; " + distances.getDistance());

        return distances;
    }

    private void playMusic(String trackName) {
        if (trackNumber <= 2) {
            trackNumber = trackNumber + 1;
        } else {
            trackNumber = 1;
        }

        try {
            ProcessManager.clear();
            Process process = new ProcessBuilder()
                    .command("afplay", musicFolderPath + trackName + trackNumber + ".mp3")
                    .start();
            process.waitFor();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}

class ProcessManager {
    private static final ProcessManager singleton = new ProcessManager();

    static void schedule(Process process) throws InterruptedException {
        singleton.scheduleProcess(process);
    }

    static void clear() {
        singleton.clearProcesses();
    }

    private synchronized void scheduleProcess(Process newProcess) throws InterruptedException {
        clear();

        System.out.println(" -------------------------------------- Process STARTED!!!! ____ " + newProcess);
        newProcess.waitFor();
    }

    private synchronized void clearProcesses() {
        Process process = null;
        try {
            process = new ProcessBuilder()
                    .command("killall", "afplay")
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner s = new Scanner(process.getErrorStream()).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        System.out.println("Error " + result);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
