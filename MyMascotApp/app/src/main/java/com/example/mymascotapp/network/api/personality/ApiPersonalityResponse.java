package com.example.mymascotapp.network.api.personality;

import android.util.Log;

import java.util.List;

public class ApiPersonalityResponse {
    protected static final String TAG = "ApiPersonalityResponse";

    private List<Personality> content;

    public ApiPersonalityResponse(List<Personality> content) {
        Log.d("FLOW", "ApiPersonalityResponse");
        this.content = content;
    }

    public List<Personality> getContent() {
        return content;
    }
}