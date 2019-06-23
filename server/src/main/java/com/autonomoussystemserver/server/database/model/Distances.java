package com.autonomoussystemserver.server.database.model;

/*
In our database we will have 2 tables (Devices and Distances)
Distances table will hold
id -> a unique auto-generated Id of a distance between two devices(TYPE: integer)
from -> which is the Id of a device which measures a distance from itself TO the id of a device in column "to" (TYPE: integer)
    distances.from has a reference to a devices.id
to -> the id of a device till which the "from" measures the distance (TYPE: integer)
    distances.to has a reference to a devices.id
distance -> the actual distances in cm (TYPE: integer)
    For example, FROM phone 5 TO phone 4 the DISTANCE is 45 cm
*/

import javax.persistence.*;

@Entity
@Table(name = "distances")
public class Distances {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "distanceId", unique = true, nullable = false, updatable = false)
    private Integer distanceId; // Hibernate will generate an id of Integer

    @ManyToOne
    @JoinColumn(name = "fromDevice") // Foreign key for device.deviceID
    private Devices fromDevice;

    @ManyToOne
    @JoinColumn(name = "toDevice") // Foreign key for device.deviceID
    private Devices toDevice;

    @Column(name = "distance", nullable = false) // in cm
    private long distance;

    public Integer getDistanceId() {
        return distanceId;
    }

    public void setDistanceId(Integer distanceId) {
        this.distanceId = distanceId;
    }

    public Devices getFromDevice() {
        return fromDevice;
    }

    public void setFromDevice(Devices fromDevice) {
        this.fromDevice = fromDevice;
    }

    public Devices getToDevice() {
        return toDevice;
    }

    public void setToDevice(Devices toDevice) {
        this.toDevice = toDevice;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }
}
