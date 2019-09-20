package com.example.mymascotapp.network.api.interaction;

import android.util.Log;

import java.util.List;

public class ApiInteractionResponse {

    protected static final String TAG = "ApiInteractionResponse";
    private List<Interaction> content;

    public ApiInteractionResponse(List<Interaction> content) {
        this.content = content;
        Log.d("FLOW", "ApiInteractionResponse");
    }

    public List<Interaction> getContent() {
//        Log.d(TAG, "content = " + content.get(0).getMascotId() + "; " + content.get(0).getInteractionTimes());
        return content;
    }
}
