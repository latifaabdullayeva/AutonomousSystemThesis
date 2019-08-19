package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DeviceDto;
import com.autonomoussystemserver.server.controller.model.DistanceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.Distances;
import com.autonomoussystemserver.server.database.model.Personality;
import com.autonomoussystemserver.server.database.repository.DevicesRepository;
import com.autonomoussystemserver.server.database.repository.DistancesRepository;
import com.autonomoussystemserver.server.database.repository.HueRepository;
import com.autonomoussystemserver.server.database.repository.PersonalityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// GET --> POST
@RestController
public class DistancesController {

    @Autowired
    private DistancesRepository distancesRepository;

    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private PersonalityRepository personalityRepository;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DistancesController.class);
//  Show logs in the terminal ->
//  LOGGER.info("here-2");

    @GetMapping("/distances")
    public org.springframework.data.domain.Page<Distances> getDistances(Pageable pageable) {


        System.out.println("Backend: " + "DistanceController -> GET getDistances");
        return distancesRepository.findAll(pageable);
    }

    @PostMapping("/distances")
    public Distances postDistance(@RequestBody DistanceDto distanceDto) {
        System.out.println("Backend: " + "DistanceController -> POST postDistance");
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
        // if distance in DB is less than 45cm, then turn the light on

        distancesRepository.save(distances);
        System.out.println("Backend: " + "DistanceController -> POST distances: " + distances);

        // TODO: get Hue data from DB
        HueRepository hueRepository = new HueRepository(
                "192.168.0.100",
                "vY5t4oArH-K0BUA7430cb1rJ8mC1DYMzkmBWRr91");
        System.out.println("Backend: " + "Hue hueRepository: " + hueRepository);
        System.out.println("Backend: " + "Hue distances.getDistance(): " + distances.getDistance());

        Devices devNameTo = devicesRepository
                .findById(distanceDto.getToDevice())
                .orElse(null);

        Devices devNameFrom = devicesRepository
                .findById(distanceDto.getFromDevice())
                .orElse(null);

        System.out.println("Backend: " + "DistanceController Personality devNameTo.getDeviceId() = " + devNameTo.getDeviceId());
        System.out.println("Backend: " + "DistanceController Personality devNameFrom.getDeviceId() = " + devNameFrom.getDeviceId());
        System.out.println("Backend: " + "DistanceController Personality devNameTo.getDeviceName() = " + devNameTo.getDeviceName());
        System.out.println("Backend: " + "DistanceController Personality devNameFrom.getDeviceName() = " + devNameFrom.getDeviceName());
        System.out.println("Backend: " + "DistanceController Personality devNameTo.getDevicePersonality() = " + devNameTo.getDevicePersonality());
        System.out.println("Backend: " + "DistanceController Personality devNameFrom.getDevicePersonality() = " + devNameFrom.getDevicePersonality().getPersonality_name());

        if (devNameTo.getDeviceName().equals("Lamp")) {
            if (distances.getDistance() <= 45) { // >= 120 && distances.getDistance() <= 370

                // TODO: get color based on personality from DB
                // !!!!! ERROR NULL -> potomu chto on v IF cikl zaxodit tolko esli device = Lamp, a u lamp net lichnosti :(
                // tebe nado lichnost ne ToDevice, a FromDEvice

                String personalityNameofDev = devNameFrom.getDevicePersonality().getPersonality_name();
                System.out.println("Backend: " + "DistanceController Personality personalityNameofDev = " + personalityNameofDev);
                Personality personality = personalityRepository.findByPersonalityName(personalityNameofDev);
                System.out.println("Backend: " + "DistanceController Personality personality = " + personality);

                int brightness = personality.getBri();
                int hue = personality.getHue();
                int saturation = personality.getSat();
//              if (brightness > 255) {brightness = 255;}

                hueRepository.updateBrightness(brightness, hue, saturation);

                // TODO: add here color:
                System.out.println("Backend: " + "Hue hueRepository.updateBrightness(brightness)= [" + brightness + "]");
                System.out.println("Backend: " + "Hue hueRepository.updateBrightness(hue)= [" + hue + "]");
                System.out.println("Backend: " + "Hue hueRepository.updateBrightness(saturation)= [" + saturation + "]");
            }
        }
        return distances;
    }
}

/*

12958	"88cf77ce-bc91-241a-b8eb-4d041f74acdf"	"Pixel One"	"Conscientiousness"
13137	"c08b6bb5-40b7-d552-1db6-a8822ec11ed9"	"Nexus 6P"	"Agreeableness"
12235	"b0702880-a295-a8ab-f734-031a98a512de"	"Lamp"


13887	1	13137	12235
13744	68	13137	12958
13747	4	12958	12235

13886	254	3488	"orange"	"rock"	"Openness"	220	"green"	4
13887	254	49460	"violet"	"rock"	"Conscientiousness"	150	"green"	4
13888	254	57805	"pink"	"rock"	"Extroversion"	198	"green"	4
13889	254	47110	"blue"	"rock"	"Agreeableness"	253	"green"	4
13890	254	12828	"yellow"	"rock"	"Neuroticism"	52	"green"	4

 */