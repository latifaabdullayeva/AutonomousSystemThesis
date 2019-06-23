package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DistanceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.Distances;
import com.autonomoussystemserver.server.database.repository.DistancesRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class DistancesController {

    @Autowired
    private DistancesRepository distancesRepository;

    //      Show logs in the terminal ->
    private static final Logger LOGGER = LoggerFactory.getLogger(DistancesController.class);
//  LOGGER.info("here-2");

    @GetMapping("/distances")
    public Page<Distances> getDistances(Pageable pageable) {
        return distancesRepository.findAll(pageable);
    }

    @PostMapping("/distances")
    public ResponseEntity<Distances> postDistance(@RequestBody DistanceDto distanceDto) {
        // if the distances between two objects exists, delete this row, and then post a new distance value
        // if the values of FROM or TO (i.e the objects are do not exists in database), do not do POST request
//        distancesRepository.delete(distanceDto.getFromDevice(), distanceDto.getToDevice());
//        distancesRepository.delete(distanceDto.getToDevice(), distanceDto.getFromDevice());

        Distances existingEndPoints = distancesRepository.findByFromAndTo(distanceDto.getFromDevice(), distanceDto.getToDevice());

        if (existingEndPoints != null) {
            Distances newDistances = new Distances();
            newDistances.setDistance(distanceDto.getDistance());
            return ResponseEntity.ok(newDistances);

//            return ResponseEntity.badRequest()
//                    .body(null);
        } else {

            Devices newFromDevice = new Devices();
            newFromDevice.setDeviceId(distanceDto.getFromDevice());

            Devices newToDevice = new Devices();
            newToDevice.setDeviceId(distanceDto.getToDevice());

            Distances newDistances = new Distances();
            newDistances.setFromDevice(newFromDevice);
            newDistances.setToDevice(newToDevice);
            newDistances.setDistance(distanceDto.getDistance());

            distancesRepository.save(newDistances);

            return ResponseEntity.ok(newDistances);
        }
    }

}