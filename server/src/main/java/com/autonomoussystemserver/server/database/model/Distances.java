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
    @Column(name = "device_id", unique = true, nullable = false, updatable = false)
    private Integer device_id; // Hibernate will generate an id of Integer


    @ManyToOne
    @JoinColumn(name = "from_device") // Foreign key for device.deviceID
    private Devices from_device;

    @ManyToOne
    @JoinColumn(name = "to_device") // Foreign key for device.deviceID
    private Devices to_device;


    @Column(name = "distance", nullable = false) // in cm
    private int distance;

    public Integer getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    public Devices getFrom_device() {
        return from_device;
    }

    public void setFrom_device(Devices from_device) {
        this.from_device = from_device;
    }

    public Devices getTo_device() {
        return to_device;
    }

    public void setTo_device(Devices to_device) {
        this.to_device = to_device;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
