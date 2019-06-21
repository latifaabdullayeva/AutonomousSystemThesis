package com.autonomoussystemserver.server.controller.model;

public class DistanceDto {
    private Integer from_device;
    private Integer to_device;
    private int distance;

    public DistanceDto(Integer from_device, Integer to_device, int distance) {
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

    public int getDistance() {
        return distance;
    }
}
