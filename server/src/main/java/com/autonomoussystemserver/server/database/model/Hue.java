package com.autonomoussystemserver.server.database.model;

import javax.persistence.*;

@Table(name = "hue")
@Entity
public class Hue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deviceId", unique = true, nullable = false, updatable = false)
    private Integer id;

    @Column(name = "ipAddress", nullable = false, updatable = false)
    private String ipAddress;

    @Column(name = "userName", updatable = false)
    private String userName;

    public Integer getId() {
        System.out.println("Backend: " + "Hue getId: " + id);
        return id;
    }

    public void setId(Integer id) {
        System.out.println("Backend:" + " Hue setId: " + id);
        this.id = id;
    }

    public String getIpAddress() {
        System.out.println("Backend: " + "Hue getIpAddress: " + ipAddress);
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        System.out.println("Backend: " + "Hue setIpAddress: " + ipAddress);
        this.ipAddress = ipAddress;
    }

    public String getUserName() {
        System.out.println("Backend: " + "Hue getUserName: " + userName);
        return userName;
    }

    public void setUserName(String userName) {
        System.out.println("Backend: " + "Hue setUserName: " + userName);
        this.userName = userName;
    }

}
