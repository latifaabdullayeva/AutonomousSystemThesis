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
public class Devices {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deviceId", unique = true, nullable = false, updatable = false)
    private Integer deviceId; // Hibernate will generate an id of the Integer

    @Column(name = "deviceName", nullable = false, updatable = false)
    private String deviceName;

    @Column(name = "beaconUuid", updatable = false)
    private String beaconUuid;

    @Column(name = "devicePersonality", updatable = false)
    private String devicePersonality;

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBeaconUuid() {
        return beaconUuid;
    }

    public void setBeaconUuid(String beaconUuid) {
        this.beaconUuid = beaconUuid;
    }

    public String getDevicePersonality() {
        return devicePersonality;
    }

    public void setDevicePersonality(String devicePersonality) {
        this.devicePersonality = devicePersonality;
    }
}