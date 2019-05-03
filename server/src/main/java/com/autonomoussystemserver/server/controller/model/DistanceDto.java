package com.autonomoussystemserver.server.controller.model;

public class DistanceDto {
    private Integer from;
    private Integer to;
    private int distance;

    public DistanceDto(Integer from, Integer to, int distance) {
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

    public int getDistance() {
        return distance;
    }
}
