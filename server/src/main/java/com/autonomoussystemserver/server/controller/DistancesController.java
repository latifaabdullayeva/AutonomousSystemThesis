package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.domainModel.Distances;
import com.autonomoussystemserver.server.exception.ResourceNotFoundException;
import com.autonomoussystemserver.server.repository.DistancesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

// GET --> ADD --> UPDATE --> DELETE
@RestController
public class DistancesController {
    @Autowired
    private DistancesRepository distancesRepository;

    @GetMapping("/distances")
    public org.springframework.data.domain.Page<Distances> getDistances(Pageable pageable) {
        return distancesRepository.findAll(pageable);
    }

    @PostMapping("/distances")
    public Distances addDistance(@RequestBody Distances distances) {
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
                    distancesRequest.setFrom(distancesRequest.getFrom());
                    distancesRequest.setTo(distancesRequest.getTo());
                    distancesRequest.setDistance(distancesRequest.getDistance());
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