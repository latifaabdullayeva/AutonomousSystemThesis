package com.example.autonomoussystemthesis.network.api.devices;

import java.util.List;

public class ApiDevicesResponse {

    private List<Device> content;

    public ApiDevicesResponse(List<Device> content) {
        this.content = content;
    }

    public List<Device> getContent() {
        return content;
    }

}