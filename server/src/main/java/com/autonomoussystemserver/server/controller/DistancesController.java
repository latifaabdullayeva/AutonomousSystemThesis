package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DistanceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.Distances;
import com.autonomoussystemserver.server.database.repository.DistancesRepository;
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

    @GetMapping("/distances")
    public org.springframework.data.domain.Page<Distances> getDistances(Pageable pageable) {
        return distancesRepository.findAll(pageable);
    }

    @PostMapping("/distances")
    public Distances postDistance(@RequestBody DistanceDto distanceDto) {
        // if the distances between two objects exists, delete this row, and then post a new distance value
        // if the values of FROM or TO (i.e the objects are do not exists in database), do not do POST request
        distancesRepository.delete(distanceDto.getFrom_device(), distanceDto.getTo_device());
        distancesRepository.delete(distanceDto.getTo_device(), distanceDto.getFrom_device());

        Devices from_device = new Devices();
        Devices to_device = new Devices();

        from_device.setDevice_id(distanceDto.getFrom_device());
        to_device.setDevice_id(distanceDto.getTo_device());

        Distances distances = new Distances();
        distances.setFrom_device(from_device);
        distances.setTo_device(to_device);
        distances.setDistance(distanceDto.getDistance());

        distancesRepository.save(distances);
        return distances;
    }
}