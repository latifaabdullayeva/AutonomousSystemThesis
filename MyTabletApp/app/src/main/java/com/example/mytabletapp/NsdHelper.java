package com.example.mytabletapp;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.util.Log;

class NsdHelper {

    private NsdManager nsdManager;
    private static final String SERVICE_TYPE = "_socialiot._tcp";

    public NsdHelper(Context context) {

    }

    void discoverServices() {
        String TAG = "ServiceActivity";
        Log.d(TAG, "discoverServices()");

    }
}
