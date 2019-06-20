package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DeviceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.repository.DevicesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    public org.springframework.data.domain.Page<Devices> getDevices(Pageable pageable) {
        return devicesRepository.findAll(pageable);
    }

    @PostMapping("/devices")
    public Devices postDevice(@RequestBody DeviceDto deviceDto) {

        devicesRepository.delete(deviceDto.getDevice_name());

        Devices devices = new Devices();
        devices.setDevice_name(deviceDto.getDevice_name());
        devices.setBeacon_tag(deviceDto.getBeacon_tag());
        devices.setDevice_personality(deviceDto.getDevice_personality());

        devicesRepository.save(devices);
        return devices;

    }

}
