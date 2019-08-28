package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DistanceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.Distances;
import com.autonomoussystemserver.server.database.model.Hue;
import com.autonomoussystemserver.server.database.model.Personality;
import com.autonomoussystemserver.server.database.repository.DevicesRepository;
import com.autonomoussystemserver.server.database.repository.DistancesRepository;
import com.autonomoussystemserver.server.database.repository.PersonalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.corba.Bridge;

import javax.swing.text.View;

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
    private com.autonomoussystemserver.server.database.repository.HueRepository hueRepository;

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

        distancesRepository.save(distances);
        System.out.println("Backend: " + "DistanceController -> POST distances: " + distances);
        System.out.println("Backend: " + "Hue distances.getDistance(): " + distances.getDistance());

//        Bridge bridge = new BridgeBuilder("appname", "devicename")
//                .setConnectionType(BridgeConnectionType.LOCAL)
//                .setBridgeConnectionProtocol(BridgeConnectionProtocol.HTTPS)
//                .setIpAddress(searchResult.getIP())
//                .setBridgeId(searchResult.getUniqueID())
//                .addBridgeStateUpdatedCallback(bridgeStateUpdateCallback)
//                .setBridgeConnectionCallback(bridgeConnectionCallback)
//                .build();

        // TODO: We find by IpAddress, but from where we get this IpAddress? get IP from  https://discovery.meethue.com
        Hue hueData = hueRepository.findByIpAddress("192.168.0.102");
        hueData.setIpAddress(hueData.getIpAddress());
        hueData.setUserName(hueData.getUserName());
        System.out.println("Backend: " + "Hue hueData.getIpAddress(): " + hueData.getIpAddress() + "; hueData.getUserName(): " + hueData.getUserName());
        HueRepository hueRepository = new HueRepository(hueData.getIpAddress(), hueData.getUserName());
        // HueRepository hueRepository = new HueRepository("192.168.0.100", "vY5t4oArH-K0BUA7430cb1rJ8mC1DYMzkmBWRr91");

        Devices devNameTo = devicesRepository.findById(distanceDto.getToDevice()).orElse(null);
        Devices devNameFrom = devicesRepository.findById(distanceDto.getFromDevice()).orElse(null);

        System.out.println("Backend: " + "DistanceController Personality devNameTo.getDeviceId() = " + devNameTo.getDeviceId() + "devNameFrom.getDeviceId() = " + devNameFrom.getDeviceId() + "devNameTo.getDeviceName() = " + devNameTo.getDeviceName() + "devNameFrom.getDeviceName() = " + devNameFrom.getDeviceName() + "devNameTo.getDevicePersonality() = " + devNameTo.getDevicePersonality() + "devNameFrom.getDevicePersonality() = " + devNameFrom.getDevicePersonality().getPersonality_name());

        if (devNameTo.getDeviceName().equals("Lamp")) {
            if (distances.getDistance() >= 120 && distances.getDistance() <= 370) { //
                String personalityNameofDev = devNameFrom.getDevicePersonality().getPersonality_name();
                Personality personality = personalityRepository.findByPersonalityName(personalityNameofDev);
                System.out.println("Backend: " + "DistanceController Personality personality = " + personality + "; personalityNameofDev = " + personalityNameofDev);
                int brightness = personality.getBri();
                int hue = personality.getHue();
                int saturation = personality.getSat();
                hueRepository.updateBrightness(brightness, hue, saturation);
                System.out.println("Backend: " + "Hue 1 hueData.getIpAddress(): " + hueData.getIpAddress() + "; hueData.getUserName()" + hueData.getUserName());
                System.out.println("Backend: " + "Hue hueRepository.updateBrightness() brightness = [" + brightness + "]; hue = [" + hue + "]; saturation = [" + saturation + "]");
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