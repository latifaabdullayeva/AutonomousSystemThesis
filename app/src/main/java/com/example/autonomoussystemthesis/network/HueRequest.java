package com.example.autonomoussystemthesis.network;

public class HueRequest {
    private boolean on;
    private int bri;

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getBrightness() {
        return bri;
    }

    public void setBrightness(int bri) {
        this.bri = bri;
    }
}
