package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DeviceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.Personality;
import com.autonomoussystemserver.server.database.repository.DevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// GET --> POST
@RestController
public class DevicesController {

    // The @Autowired annotation allows you to skip configurations elsewhere of what to inject and just does it for you
    @Autowired
    private DevicesRepository devicesRepository;

    // maps HTTP GET requests onto specific handler methods. It is a composed annotation that acts as a shortcut
    // for @RequestMapping(method = RequestMethod.GET).
    @GetMapping("/devices")
    public Page<Devices> getDevices(Pageable pageable) {
        System.out.println("Backend: " + "DeviceController -> GET getDevices()");
        return devicesRepository.findAll(pageable);
    }

    @PostMapping("/devices")
    public ResponseEntity<Devices> createDevice(@RequestBody DeviceDto deviceDto) {
        System.out.println("Backend: " + "DevicesController -> POST createDevice()");

        Devices existingDevice = devicesRepository.findByBeacon(deviceDto.getBeaconUuid());

        if (existingDevice != null) {
            return ResponseEntity.badRequest()
                    .body(null);
        } else {
            Personality personality= new Personality();
            personality.setId(deviceDto.getDevicePersonality());

            Devices newDevice = new Devices();
            System.out.println("Backend: " + "DevicesController -> POST deviceDto.getDeviceName() = " + deviceDto.getDeviceName());
            newDevice.setDeviceName(deviceDto.getDeviceName());
            System.out.println("Backend: " + "DevicesController -> POST deviceDto.getBeaconUuid() = " + deviceDto.getBeaconUuid());
            newDevice.setBeaconUuid(deviceDto.getBeaconUuid());
            System.out.println("Backend: " + "DevicesController -> POST personality = " + personality);
            newDevice.setDevicePersonality(personality);

            devicesRepository.save(newDevice);
            System.out.println("Backend: " + "DevicesController -> POST newDevice: " + newDevice);
            return ResponseEntity.ok(newDevice);
        }
    }
}
