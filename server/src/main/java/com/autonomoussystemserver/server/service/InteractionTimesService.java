//package com.autonomoussystemserver.server.service;
//
//import com.autonomoussystemserver.server.controller.model.InteractionTimesDto;
//import com.autonomoussystemserver.server.database.model.Devices;
//import com.autonomoussystemserver.server.database.model.InteractionTimes;
//import com.autonomoussystemserver.server.database.repository.InteractionTimesRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class InteractionTimesService {
//
//    @Autowired
//    private InteractionTimesRepository interactionTimesRepository;
//
//    // update or insert
//    public InteractionTimes incrementInteractionTimes(InteractionTimesDto interactionTimesDto) {
//        if (interactionTimesDto.getMascotId() == null) {
//            return null;
//        }
//
//        Devices device = new Devices();
//        device.setDeviceId(interactionTimesDto.getMascotId());
//
//        InteractionTimes interaction = interactionTimesRepository.findByMascot(device);
//
//        if (interaction == null) {
//            interaction = new InteractionTimes();
//
//            interaction.setMascot(device);
//            interaction.setInteractionTimes(1);
//        } else {
//            interaction.setInteractionTimes(
//                    interaction.getInteractionTimes() + 1);
//        }
//
//        interactionTimesRepository.save(interaction);
//        return interaction;
//    }
//}