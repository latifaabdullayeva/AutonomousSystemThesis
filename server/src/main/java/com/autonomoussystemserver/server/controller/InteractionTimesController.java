//package com.autonomoussystemserver.server.controller;
//
//import com.autonomoussystemserver.server.controller.model.InteractionTimesDto;
//import com.autonomoussystemserver.server.database.model.InteractionTimes;
//import com.autonomoussystemserver.server.database.repository.InteractionTimesRepository;
//import com.autonomoussystemserver.server.service.InteractionTimesService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//public class InteractionTimesController {
//
//    @Autowired
//    private InteractionTimesRepository interactionTimesRepository;
//
//    @Autowired
//    private InteractionTimesService interactionTimesService;
//
//    @GetMapping("/interactionTimes")
//    public Page<InteractionTimes> getInteractionTimes(Pageable pageable) {
//        System.out.println("InteractionTimesController: getInteractionTimes()");
//        return interactionTimesRepository.findAll(pageable);
//    }
//
//    @PostMapping("/interactionTimes")
//    public ResponseEntity<InteractionTimes> createInteraction(@RequestBody InteractionTimesDto interactionTimesDto) {
//        System.out.println("InteractionTimesController: createInteraction()");
//
//        InteractionTimes interactionTimes = interactionTimesService
//                .incrementInteractionTimes(interactionTimesDto);
//
//        if (interactionTimes == null) {
//            return ResponseEntity.badRequest()
//                    .body(null);
//        }
//
//        return ResponseEntity.ok(interactionTimes);
//    }
//}