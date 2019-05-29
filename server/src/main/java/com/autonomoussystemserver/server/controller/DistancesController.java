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
        distancesRepository.delete(distanceDto.getFrom(), distanceDto.getTo());
        distancesRepository.delete(distanceDto.getTo(), distanceDto.getFrom());

        Devices fromDevice = new Devices();
        Devices toDevice = new Devices();

        fromDevice.setId(distanceDto.getFrom());
        toDevice.setId(distanceDto.getTo());

        Distances distances = new Distances();
        distances.setFrom(fromDevice);
        distances.setTo(toDevice);
        distances.setDistance(distanceDto.getDistance());

        distancesRepository.save(distances);
        return null; // return distances; esli tebe dlya muzike ponadobitsa
    }
}