package com.example.autonomoussystemthesis.network.api.distance;

public class Distance {
    private final Integer from;
    private final Integer to;
    private final double distance;

    public Distance(Integer from, Integer to, double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public double getDistance() {
        return distance;
    }
}
