package com.example.autonomoussystemthesis.network.api.distance;

import android.util.Log;

import java.util.List;

public class ApiDistanceResponse {
    protected static final String TAG = "ApiDistanceResponse";
    private List<Distance> content;

    public ApiDistanceResponse(List<Distance> content) {
        this.content = content;
        Log.d("FLOW", "ApiDistanceResponse");
    }

    public List<Distance> getContent() {
        Log.d(TAG, "content = " + content.get(0).getFromDevice() + "; " + content.get(0).getToDevice());
        return content;
    }
}
