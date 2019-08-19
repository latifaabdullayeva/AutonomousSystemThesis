package com.autonomoussystemserver.server.controller;

public class HueRequest {
    private final boolean on;
    private final int bri;
    private final int hue;
    private final int sat;

    public HueRequest(boolean on, int bri, int hue, int sat) {
        this.on = on;
        this.bri = bri;
        this.hue = hue;
        this.sat = sat;
        System.out.println("Backend: " + "HueRequest contructor");
    }

    public boolean isOn() {
        System.out.println("Backend: " + "HueRequest isOn()");
        return on;
    }

    public int getBri() {
        System.out.println("Backend: " + "HueRequest getBri()");
        return bri;
    }

    public int getHue() {
        System.out.println("Backend: " + "HueRequest getHue()");
        return hue;
    }

    public int getSat() {
        System.out.println("Backend: " + "HueRequest getSat()");
        return sat;
    }
}
