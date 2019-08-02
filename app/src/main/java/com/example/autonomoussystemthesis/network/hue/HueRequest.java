package com.example.autonomoussystemthesis.network.hue;

public class HueRequest {
    private final boolean on;
    private final int bri;

    HueRequest(boolean on, int bri) {
        this.on = on;
        this.bri = bri;
    }

    public boolean isOn() {
        return on;
    }

    public int getBri() {
        return bri;
    }
}
