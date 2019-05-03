package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DistanceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.Distances;
import com.autonomoussystemserver.server.database.repository.DistancesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

// GET --> POST
@RestController
public class DistancesController {

    @Autowired
    private DistancesRepository distancesRepository;

//    private TestDistances repository;
//
//    void SomeClient(TestDistances repository) {
//        this.repository = repository;
//    }
//
//    void doSomething() {
//        List<Distances> persons = repository.findByFromAndToe("Matthews");
//    }

    @GetMapping("/distances")
    public org.springframework.data.domain.Page<Distances> getDistances(Pageable pageable) {
        return distancesRepository.findAll(pageable);
    }

    @PostMapping("/distances")
    public Distances postDistance(@RequestBody DistanceDto distanceDto) {
        Devices fromDevice = new Devices();
        fromDevice.setId(distanceDto.getFrom());

        Devices toDevice = new Devices();
        toDevice.setId(distanceDto.getTo());

        Distances distances = new Distances();
        distances.setFrom(fromDevice);
        distances.setTo(toDevice);
        distances.setDistance(distanceDto.getDistance());


        if (distanceDto.getFrom() == 1 && distanceDto.getTo() == 2) {
            distancesRepository.deleteUsingSingleQuery(1, 2);
        } else {

            System.out.println();
            System.out.println("PRINT!!!! from = " + (distanceDto.getFrom())
                    + " = " + distancesRepository.existsById((distanceDto.getFrom())));
            System.out.println("PRINT!!!! to = " + (distanceDto.getTo())
                    + " = " + distancesRepository.existsById((distanceDto.getTo())));
            return distancesRepository.save(distances);
        }
        return null;
    }
}

// 1) if distances for from and to keys exist, delete it
// 2) insert new distances
// if (!distancesRepository.existsById(distanceId)) {
//    delete existing row
//    throw new ResourceNotFoundException("Distance not found with id " + distanceId);
// }


//    @PutMapping("/distances/{distanceId}")
//    public Distances updateDistances(@PathVariable UUID distanceId,
//                                     @Valid @RequestBody Distances distancesRequest) {
//        if (!distancesRepository.existsById(distanceId)) {
//            throw new ResourceNotFoundException("Distance not found with id " + distanceId);
//        }
//        return distancesRepository.findById(distanceId)
//                .map(distances -> {
//                    distancesRequest.setFrom(distancesRequest.getFrom());
//                    distancesRequest.setTo(distancesRequest.getTo());
//                    distancesRequest.setDistance(distancesRequest.getDistance());
//                    return distancesRepository.save(distances);
//                }).orElseThrow(() -> new ResourceNotFoundException("Distance not found with id " + distanceId));
//    }

//// udali, klient ne budet nicheqo udalat, toje samoe i PUT
//    @DeleteMapping("/distances/{distanceId}")
//    public ResponseEntity<?> deleteDistances(@PathVariable UUID distanceId) {
//        if (!distancesRepository.existsById(distanceId)) {
//            throw new ResourceNotFoundException("Distance not found with id " + distanceId);
//        }
//        return distancesRepository.findById(distanceId)
//                .map(distances -> {
//                    distancesRepository.delete(distances);
//                    return ResponseEntity.ok().build();
//                }).orElseThrow(() -> new ResourceNotFoundException("Distance not found with id " + distanceId));
//    }