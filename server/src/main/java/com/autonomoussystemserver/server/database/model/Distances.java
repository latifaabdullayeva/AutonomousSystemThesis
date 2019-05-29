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
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Integer id; // Hibernate will generate an id of Integer,
    // TODO: If you want change to UUID -> the form “8dd5f315-9788-4d00-87bb-10eed9eff566”


    @ManyToOne
    @JoinColumn(name = "FK_from") // Foreign key for device.deviceID
    private Devices from;

    @ManyToOne
    @JoinColumn(name = "FK_to") // Foreign key for device.deviceID
    private Devices to;


    @Column(name = "distance", nullable = false) // in cm
    private int distance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Devices getFrom() {
        return from;
    }

    public void setFrom(Devices from) {
        this.from = from;
    }

    public Devices getTo() {
        return to;
    }

    public void setTo(Devices to) {
        this.to = to;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
