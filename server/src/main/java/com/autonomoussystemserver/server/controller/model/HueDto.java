package com.autonomoussystemserver.server.controller.model;

public class HueDto {
    private String ipAddress;
    private String userName;

    public HueDto(String ipAddress, String userName) {
        System.out.println("Backend: " + "HueDto constructor");
        this.ipAddress = ipAddress;
        this.userName = userName;
    }

    public String getIpAddress() {
        System.out.println("Backend: " + "HueDto getIpAddress");
        return ipAddress;
    }

    public String getUserName() {
        System.out.println("Backend: " + "HueDto getUserName");
        return userName;
    }
}
