package com.example.mymascotapp.network.api.devices;

import android.util.Log;

import java.util.List;

public class ApiDevicesResponse {
    protected static final String TAG = "ApiDevicesResponse";
    private List<Device> content;

    public ApiDevicesResponse(List<Device> content) {
        this.content = content;
        Log.d("FLOW", "ApiDevicesResponse content = " + content);
    }

    public List<Device> getContent() {
        return content;
    }

}