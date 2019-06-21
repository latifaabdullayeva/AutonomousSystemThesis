package com.example.autonomoussystemthesis.network.api.distance;

public class Distance {
    private final Integer from_device;
    private final Integer to_device;
    private final double distance;

    public Distance(Integer from_device, Integer to_device, double distance) {
        this.from_device = from_device;
        this.to_device = to_device;
        this.distance = distance;
    }

    public Integer getFrom_device() {
        return from_device;
    }

    public Integer getTo_device() {
        return to_device;
    }

    public double getDistance() {
        return distance;
    }
}
