package com.example.autonomoussystemthesis.network.api.interaction;

// interactionTimes, mascot

import android.util.Log;

public class Interaction {
    private final Integer interactionId;
    private final Integer mascotId;
    private final Integer interactionTimes;

    Interaction(Integer interactionId, Integer mascotId, Integer interactionTimes) {
        Log.d("FLOW", "Interaction");
        this.interactionId = interactionId;
        this.interactionTimes = interactionTimes;
        this.mascotId = mascotId;
    }

    public Integer getInteractionId() {
        return interactionId;
    }

    public Integer getMascotId() {
        return mascotId;
    }

    public Integer getInteractionTimes() {
        return interactionTimes;
    }
}