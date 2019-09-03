package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.database.model.InteractionTimes;
import com.autonomoussystemserver.server.database.repository.InteractionTimesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


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

}
