package com.example.autonomoussystemthesis.network.api.interaction;

// interactionTimes, mascot

import android.util.Log;

public class Interaction {
    private final Integer interactionTimes;
    private final Integer mascotId;

    Interaction(Integer interactionTimes, Integer mascotId) {
        Log.d("FLOW", "Interaction");

        this.interactionTimes = interactionTimes;
        this.mascotId = mascotId;
    }

    public Integer getInteractionTimes() {
        return interactionTimes;
    }

    public Integer getMascotId() {
        return mascotId;
    }
}
