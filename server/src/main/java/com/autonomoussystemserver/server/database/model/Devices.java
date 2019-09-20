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

    @Column(name = "deviceType", nullable = false, updatable = false)
    private String deviceType;

    @Column(name = "beaconUuid", updatable = false)
    private String beaconUuid;

    @ManyToOne
    @JoinColumn(name = "devicePersonality", updatable = false) // // Foreign key for personality.personality_id
    private Personality devicePersonality;

    public Integer getDeviceId() {
//        System.out.println("---------");
//        System.out.println("Devices getDeviceId = " + deviceId);
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
//        System.out.println("Devices setDeviceId = " + deviceId);
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
//        System.out.println("Devices getDeviceName = " + deviceName);
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
//        System.out.println("Devices setDeviceName = " + deviceName);
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
//        System.out.println("Devices getDeviceType = " + deviceType);
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
//        System.out.println("Devices setDeviceType = " + deviceType);
        this.deviceType = deviceType;
    }

    public String getBeaconUuid() {
//        System.out.println("Devices getBeaconUuid = " + beaconUuid);
        return beaconUuid;
    }

    public void setBeaconUuid(String beaconUuid) {
//        System.out.println("Devices setBeaconUuid = " + beaconUuid);
        this.beaconUuid = beaconUuid;
    }

    public Personality getDevicePersonality() {
        return devicePersonality;
    }

    public void setDevicePersonality(Personality devicePersonality) {
        this.devicePersonality = devicePersonality;
    }
}