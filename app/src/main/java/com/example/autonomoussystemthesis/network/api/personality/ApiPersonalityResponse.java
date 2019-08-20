package com.example.autonomoussystemthesis.network.api.personality;

import android.util.Log;

import java.util.List;

public class ApiPersonalityResponse {
    protected static final String TAG = "ApiPersonalityResponse";

    private List<Personality> content;

    public ApiPersonalityResponse(List<Personality> content) {
        this.content = content;
        Log.d(TAG, "Personality API");
    }

    public List<Personality> getContent() {
        Log.d(TAG, "Personality Api getContent()");
        return content;
    }
}