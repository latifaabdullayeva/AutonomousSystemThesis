package com.autonomoussystemserver.server.controller.model;

public class DistanceDto {
    private Integer fromDevice;
    private Integer toDevice;
    private Integer distance;

    public DistanceDto(Integer fromDevice, Integer toDevice, Integer distance) {
        this.fromDevice = fromDevice;
        this.toDevice = toDevice;
        this.distance = distance;
        System.out.println("Backend: " + "DistanceDto constructor");
    }

    public Integer getFromDevice() {
        System.out.println("Backend: " + "DistanceDto getFromDevice");
        return fromDevice;
    }

    public Integer getToDevice() {
        System.out.println("Backend: " + "DistanceDto getToDevice");
        return toDevice;
    }

    public Integer getDistance() {
        System.out.println("Backend: " + "DistanceDto getDistance");
        return distance;
    }
}
