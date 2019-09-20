package com.autonomoussystemserver.server.controller.model;

public class DistanceDto {
    private Integer fromDevice;
    private Integer toDevice;
    private Integer distance;

    // for deserialisation
    public DistanceDto() {
    }

    public DistanceDto(Integer fromDevice, Integer toDevice, Integer distance) {
        this.fromDevice = fromDevice;
        this.toDevice = toDevice;
        this.distance = distance;
//        System.out.println("DistanceDto constructor");
    }

    public Integer getFromDevice() {
//        System.out.println("DistanceDto getFromDevice = " + fromDevice);
        return fromDevice;
    }

    public Integer getToDevice() {
//        System.out.println("DistanceDto getToDevice = " + toDevice);
        return toDevice;
    }

    public Integer getDistance() {
//        System.out.println("DistanceDto getDistance = " + distance);
        return distance;
    }
}
