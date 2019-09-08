package com.autonomoussystemserver.server.controller.model;

public class HueDto {
    private String ipAddress;
    private String userName;

    // for deserialisation
    public HueDto() {
    }

    public HueDto(String ipAddress, String userName) {
        System.out.println("HueDto constructor");
        this.ipAddress = ipAddress;
        this.userName = userName;
    }

    public String getIpAddress() {
        System.out.println("HueDto getIpAddress = " + ipAddress);
        return ipAddress;
    }

    public String getUserName() {
        System.out.println("HueDto getUserName = " + userName);
        return userName;
    }
}
