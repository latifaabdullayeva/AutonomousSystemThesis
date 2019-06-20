package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.repository.DevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("DeviceService")
public class DeviceService {

    private DevicesRepository devicesRepository;

    @Autowired
    public DeviceService(DevicesRepository devicesRepository) {
        this.devicesRepository = devicesRepository;
    }

    public Devices findByBeacon(String beacon_tag) {
        return devicesRepository.findByBeacon(beacon_tag);
    }
}
