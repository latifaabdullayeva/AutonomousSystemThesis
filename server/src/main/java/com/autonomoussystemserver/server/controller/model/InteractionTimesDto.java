package com.autonomoussystemserver.server.controller.model;

public class InteractionTimesDto {

    private Integer mascotId;
    private Integer interactionTimes;

    // for deserialisation
    public InteractionTimesDto() {
    }

    public InteractionTimesDto(Integer mascotId, Integer interactionTimes) {
        this.mascotId = mascotId;
        this.interactionTimes = interactionTimes;
    }

    public Integer getMascotId() {
        return mascotId;
    }

    public Integer getInteractionTimes() {
        return interactionTimes;
    }
}
