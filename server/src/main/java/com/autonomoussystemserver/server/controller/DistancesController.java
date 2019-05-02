package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.domainModel.Devices;
import com.autonomoussystemserver.server.domainModel.Distances;
import com.autonomoussystemserver.server.exception.ResourceNotFoundException;
import com.autonomoussystemserver.server.repository.DevicesRepository;
import com.autonomoussystemserver.server.repository.DistancesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class DistancesController {
    @Autowired
    private DistancesRepository distancesRepository;

    @GetMapping("/distances")
    public org.springframework.data.domain.Page<Distances> getDistances(Pageable pageable) {
        return distancesRepository.findAll(pageable);
    }

    @PostMapping("/distances")
    public Distances addDistance(@Valid @RequestBody Distances distances) {
        return distancesRepository.save(distances);
    }

    @PutMapping("/distances/{distanceId}")
    public Distances updateDistances(@PathVariable UUID distanceId,
                                     @Valid @RequestBody Distances distancesRequest) {
        if (!distancesRepository.existsById(distanceId)) {
            throw new ResourceNotFoundException("Distance not found with id " + distanceId);
        }
        return distancesRepository.findById(distanceId)
                .map(distances -> {
                    distances.setFrom(distancesRequest.getFrom());
                    distances.setTo(distancesRequest.getTo());
                    distances.setDistance(distancesRequest.getDistance());
                    return distancesRepository.save(distances);
                }).orElseThrow(() -> new ResourceNotFoundException("Distance not found with id " + distanceId));
    }


    @DeleteMapping("/distances/{distanceId}")
    public ResponseEntity<?> deleteDistances(@PathVariable UUID distanceId) {
        if (!distancesRepository.existsById(distanceId)) {
            throw new ResourceNotFoundException("Distance not found with id " + distanceId);
        }
        return distancesRepository.findById(distanceId)
                .map(distances -> {
                    distancesRepository.delete(distances);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Distance not found with id " + distanceId));
    }
}