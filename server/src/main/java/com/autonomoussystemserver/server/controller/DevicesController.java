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

    // maps HTTP GET requests onto specific handler methods. It is a composed annotation that acts as
    // a shortcut for @RequestMapping(method = RequestMethod.GET).
    @GetMapping("/devices")
    public Page<Devices> getDevices(Pageable pageable) {
        return devicesRepository.findAll(pageable);
    }

    @PostMapping("/devices")
    public ResponseEntity<Devices> createDevice(@RequestBody DeviceDto deviceDto) {
        System.out.println("DevicesController: createDevice() from DTO:\n" + deviceDto.getBeaconUuid() +
                "; " + deviceDto.getDeviceType() + "; " + deviceDto.getDevicePersonality().getId());

        Devices existingDevice = devicesRepository.findByBeacon(deviceDto.getBeaconUuid());

        if (existingDevice != null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            Devices newDevice = new Devices();

            if (deviceDto.getDevicePersonality() != null) {
                Personality personality = new Personality();
                personality.setId(deviceDto.getDevicePersonality().getId());

                newDevice.setDevicePersonality(personality);
            }

            newDevice.setDeviceName(deviceDto.getDeviceName() == null ? "" : deviceDto.getDeviceName());
            newDevice.setDeviceType(deviceDto.getDeviceType());
            newDevice.setBeaconUuid(deviceDto.getBeaconUuid());

            devicesRepository.save(newDevice);

            System.out.println("DevicesController: newDevice = \n" + newDevice.getDeviceId() + "; "
                    + newDevice.getBeaconUuid() + "; " + newDevice.getDeviceType() + "; "
                    + newDevice.getDevicePersonality().getId());

            return ResponseEntity.ok(newDevice);
        }
    }
}

