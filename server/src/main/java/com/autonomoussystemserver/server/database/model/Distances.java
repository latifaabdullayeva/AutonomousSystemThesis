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
    private Integer distance;

    public Integer getDistanceId() {
//        System.out.println("---------");
//        System.out.println("Distances getDistanceId");
        return distanceId;
    }

    public void setDistanceId(Integer distanceId) {
//        System.out.println("Distances setDistanceId");
        this.distanceId = distanceId;
    }

    public Devices getFromDevice() {
//        System.out.println("Distances getFromDevice");
        return fromDevice;
    }

    public void setFromDevice(Devices fromDevice) {
//        System.out.println("Distances setFromDevice");
        this.fromDevice = fromDevice;
    }

    public Devices getToDevice() {
//        System.out.println("Distances getToDevice");
        return toDevice;
    }

    public void setToDevice(Devices toDevice) {
//        System.out.println("Distances setToDevice");
        this.toDevice = toDevice;
    }

    public Integer getDistance() {
//        System.out.println("Distances getDistance");
        return distance;
    }

    public void setDistance(Integer distance) {
//        System.out.println("Distances setDistance");
        this.distance = distance;
    }
}
