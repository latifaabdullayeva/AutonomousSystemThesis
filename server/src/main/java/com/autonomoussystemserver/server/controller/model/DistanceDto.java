package com.autonomoussystemserver.server.controller.model;

public class DistanceDto {
    private Integer fromDevice;
    private Integer toDevice;
    private Integer distance;

    public DistanceDto(Integer fromDevice, Integer toDevice, Integer distance) {
        this.fromDevice = fromDevice;
        this.toDevice = toDevice;
        this.distance = distance;
    }

    public Integer getFromDevice() {
        return fromDevice;
    }

    public Integer getToDevice() {
        return toDevice;
    }

    public Integer getDistance() {
        return distance;
    }
}
