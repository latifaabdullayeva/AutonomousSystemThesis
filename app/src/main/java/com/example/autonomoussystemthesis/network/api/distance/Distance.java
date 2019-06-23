package com.example.autonomoussystemthesis.network.api.distance;

public class Distance {
    private final Integer fromDevice;
    private final Integer toDevice;
    private final Long distance;

    public Distance(Integer fromDevice, Integer toDevice, Long distance) {
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

    public Long getDistance() {
        return distance;
    }
}
