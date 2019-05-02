package com.autonomoussystemserver.server.domainModel;

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
import java.util.UUID;

@Table(name = "devices")
@Entity
public class Devices {
    @Id
    @GeneratedValue
    @Column(name = "_id", unique = true, nullable = false, updatable = false, table = "devices")
    private UUID id; // Hibernate will generate an id of the form “8dd5f315-9788-4d00-87bb-10eed9eff566”

    @Column(name = "name", nullable = false, updatable = false, table = "devices")
    private String name;

    @Column(name = "personality", updatable = false, table = "devices")
    private String personality;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
