package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.InteractionTimesDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.InteractionTimes;
import com.autonomoussystemserver.server.database.repository.InteractionTimesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class InteractionTimesController {

    @Autowired
    private InteractionTimesRepository interactionTimesRepository;

    @GetMapping("/interactionTimes")
    public Page<InteractionTimes> getInteractionTimes(Pageable pageable) {
        System.out.println("------------------------------------------------------------");
        System.out.println("InteractionTimesController -> GET getInteractionTimes()");
        return interactionTimesRepository.findAll(pageable);
    }

    @PostMapping("/interactionTimes")
    public ResponseEntity<InteractionTimes> createInteraction(@RequestBody InteractionTimesDto interactionTimesDto) {
        System.out.println("------------------------------------------------------------");
        System.out.println("InteractionTimesController -> POST createInteraction()");

        if (interactionTimesDto.getMascotId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Devices device = new Devices();
        device.setDeviceId(interactionTimesDto.getMascotId());

        InteractionTimes existingInteraction = interactionTimesRepository.findByMascot(device);

        if (existingInteraction != null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            InteractionTimes newInteractionTimes = new InteractionTimes();

            newInteractionTimes.setMascotId(device);
            newInteractionTimes.setInteractionTimes(interactionTimesDto.getInteractionTimes());

            interactionTimesRepository.save(newInteractionTimes);
            return ResponseEntity.ok(newInteractionTimes);
        }
    }
}