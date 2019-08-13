package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DistanceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.Distances;
import com.autonomoussystemserver.server.database.repository.DistancesRepository;
import com.autonomoussystemserver.server.database.repository.HueRepository;
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

        HueRepository hueRepository = new HueRepository(
                "192.168.0.100",
                "vY5t4oArH-K0BUA7430cb1rJ8mC1DYMzkmBWRr91");
        System.out.println("Backend: " + "Hue hueRepository: " + hueRepository);
        System.out.println("Backend: " + "Hue distances.getDistance(): " + distances.getDistance());

        if (distances.getDistance() <= 45) {
            int brightness = distances.getDistance() * 80;
            if (brightness > 255) {
                brightness = 255;
            }
            // TODO: add here color:
            hueRepository.updateBrightness(brightness);
            System.out.println("Backend: " + "Hue hueRepository.updateBrightness(brightness): " + brightness);
        }
        return distances;
        // eshe tut nado opisat logiku o tom chtobi distanciya 45 bila imenno mejdu telefonom i Lampy, to est beacon Lampi
        // prover distanciya prinadlejit li Lampe ili net
    }
}