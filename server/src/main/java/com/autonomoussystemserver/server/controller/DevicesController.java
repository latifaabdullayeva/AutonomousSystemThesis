package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.domainModel.Devices;
import com.autonomoussystemserver.server.repository.DevicesRepository;
import com.autonomoussystemserver.server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

// GET --> POST --> DELETE
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
    public Devices createDevice(@Valid @RequestBody Devices devices) {
        return devicesRepository.save(devices);
    }

    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<?> deleteDevice(@PathVariable UUID deviceId) {
        return devicesRepository.findById(deviceId)
                .map(devices -> {
                    devicesRepository.delete(devices);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Device not found with id " + deviceId));
    }

}
