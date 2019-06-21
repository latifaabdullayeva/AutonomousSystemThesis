package com.autonomoussystemserver.server.database.model;

/*
In our database we will have 2 tables (Devices and Distances)
Devices table will hold
id -> a unique auto-generated Id of a device (phones with personalities, bench, tablet) (TYPE: UUID)
device_name -> a name (like Nexus, or Bench name) (TYPE: String)
beacon_tag -->
device_personality -> a personality of a device (if it is phone then personality type, if it is bench or tablet, then none),
since in our study only phones has personalities (TYPE: String)
name -> cannot be NULL, but personality can be, because our devices like Bench and Tablet do not have personality
*/

import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Table(name = "devices")
@Entity
public class Devices{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "device_id", unique = true, nullable = false, updatable = false)
    private Integer device_id; // Hibernate will generate an id of the Integer


    @OneToMany(mappedBy = "from_device", cascade = CascadeType.ALL)
    Set<Distances> from_device = new HashSet<Distances>();


    @OneToMany(mappedBy = "to_device", cascade = CascadeType.ALL)
    Set<Distances> to_device = new HashSet<Distances>();


    @Column(name = "device_name", nullable = false, updatable = false)
    private String device_name;

    @Column(name = "beacon_UUID", updatable = false)
    private String beacon_UUID;

    @Column(name = "device_personality", updatable = false)
    private String device_personality;

    public Integer getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getBeacon_UUID() {
        return beacon_UUID;
    }

    public void setBeacon_UUID(String beacon_UUID) {
        this.beacon_UUID = beacon_UUID;
    }

    public String getDevice_personality() {
        return device_personality;
    }

    public void setDevice_personality(String device_personality) {
        this.device_personality = device_personality;
    }
}