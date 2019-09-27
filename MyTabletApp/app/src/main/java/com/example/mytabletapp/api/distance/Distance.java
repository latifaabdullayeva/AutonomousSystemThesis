package com.example.mytabletapp.api.distance;

import com.example.mytabletapp.api.devices.Device;

public class Distance {
    private final Integer distanceId;
    private final Device fromDevice;
    private final Device toDevice;
    private final Long distance;

    public Distance(Integer id) {
        this(id, null, null, null);
    }

    Distance(Integer distanceId, Device fromDevice, Device toDevice, Long distance) {
        this.distanceId = distanceId;
        this.fromDevice = fromDevice;
        this.toDevice = toDevice;
        this.distance = distance;
    }

    public Integer getDistanceId() {
        return distanceId;
    }

    public Device getFromDevice() {
        return fromDevice;
    }

    public Device getToDevice() {
        return toDevice;
    }

    public Long getDistance() {
        return distance;
    }
}
