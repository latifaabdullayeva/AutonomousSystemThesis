package com.autonomoussystemserver.server.controller.model;

import com.autonomoussystemserver.server.database.model.Devices;

public class InteractionTimesDto {

    private Devices mascotId;
    private Integer interactionTimes;


    // for deserialisation
    public InteractionTimesDto() {
    }

    public InteractionTimesDto(Devices mascotId, Integer interactionTimes) {
        System.out.println("InteractionTimesDto constructor");
        this.mascotId = mascotId;
        this.interactionTimes = interactionTimes;
    }

    public Devices getMascotId() {
        System.out.println("InteractionTimesDto getMascotId = " + mascotId);
        return mascotId;
    }

    public Integer getInteractionTimes() {
        System.out.println("InteractionTimesDto getInteractionTimes = " + interactionTimes);
        return interactionTimes;
    }
}
