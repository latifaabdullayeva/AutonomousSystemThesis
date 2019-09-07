package com.example.autonomoussystemthesis.network.api.interaction;

// interactionTimes, mascot

import android.util.Log;

import com.example.autonomoussystemthesis.network.api.devices.Device;

public class Interaction {
    private final Integer interactionId;
    private final Device mascotId;
    private final Integer interactionTimes;

    Interaction(Integer interactionId, Device mascotId, Integer interactionTimes) {
        Log.d("FLOW", "Interaction");
        this.interactionId = interactionId;
        this.interactionTimes = interactionTimes;
        this.mascotId = mascotId;
    }

    public Integer getInteractionId() {
        return interactionId;
    }

    public Device getMascotId() {
        return mascotId;
    }

    public Integer getInteractionTimes() {
        return interactionTimes;
    }
}