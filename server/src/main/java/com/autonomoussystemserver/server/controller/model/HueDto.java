package com.autonomoussystemserver.server.controller.model;

public class HueDto {
    private String ipAddress;
    private String userName;

    // for deserialisation
    public HueDto() {
    }

    public HueDto(String ipAddress, String userName) {
        this.ipAddress = ipAddress;
        this.userName = userName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserName() {
        return userName;
    }
}
