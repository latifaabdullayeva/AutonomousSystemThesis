package com.example.mytabletapp.api.interaction;

public class Interaction {
    private final Integer mascotId;

    Interaction(Integer mascotId) {
        this.mascotId = mascotId;
    }

    public Integer getMascotId() {
        return mascotId;
    }
}