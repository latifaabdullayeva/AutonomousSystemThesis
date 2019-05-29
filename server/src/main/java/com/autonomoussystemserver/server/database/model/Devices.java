package com.autonomoussystemserver.server.database.model;

/*
In our database we will have 2 tables (Devices and Distances)
Devices table will hold
id -> a unique auto-generated Id of a device (phones with personalities, bench, tablet) (TYPE: UUID)
name -> a name (like Nexus, or Bench name) (TYPE: String)
personality -> a personality of a device (if it is phone then personality type, if it is bench or tablet, then none),
since in our study only phones has personalities (TYPE: String)
name -> cannot be NULL, but personality can be, because our devices like Bench and Tablet do not have personality
*/

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "devices")
@Entity
public class Devices {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "deviceID", unique = true, nullable = false, updatable = false)
    private Integer deviceID; // Hibernate will generate an id of the Integer
    // TODO: you can change to UUID -> form “8dd5f315-9788-4d00-87bb-10eed9eff566”


    @OneToMany(mappedBy="from", cascade = CascadeType.ALL)
    Set<Distances> from = new HashSet<Distances>();


    @OneToMany(mappedBy="to", cascade = CascadeType.ALL)
    Set<Distances> to = new HashSet<Distances>();


    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "personality", updatable = false)
    private String personality;

    public Integer getId() {
        return deviceID;
    }

    public void setId(Integer id) {
        this.deviceID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }
}