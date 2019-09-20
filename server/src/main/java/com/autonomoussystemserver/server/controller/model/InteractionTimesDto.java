package com.autonomoussystemserver.server.controller.model;

public class InteractionTimesDto {

    private Integer mascotId;
    private Integer interactionTimes;

    // for deserialisation
    public InteractionTimesDto() {
    }

    public InteractionTimesDto(Integer mascotId, Integer interactionTimes) {
//        System.out.println("InteractionTimesDto constructor");
        this.mascotId = mascotId;
        this.interactionTimes = interactionTimes;
    }

    public Integer getMascotId() {
//        System.out.println("InteractionTimesDto getMascotId = " + mascotId);
        return mascotId;
    }

    public Integer getInteractionTimes() {
//        System.out.println("InteractionTimesDto getInteractionTimes = " + interactionTimes);
        return interactionTimes;
    }
}
