package com.autonomoussystemserver.server.controller;

public class HueRequest {
    private final boolean on;
    private final int bri;
    private final int hue;
    private final int sat;

    HueRequest(boolean on, int bri, int hue, int sat) {
        this.on = on;
        this.bri = bri;
        this.hue = hue;
        this.sat = sat;
    }

    public boolean isOn() {
        return on;
    }

    public int getBri() {
        return bri;
    }

    public int getHue() {
        return hue;
    }

    public int getSat() {
        return sat;
    }
}
