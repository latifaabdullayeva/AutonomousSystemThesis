package com.example.mymascotapp;

import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public class NsdHelper {
    private NsdManager nsdManager;

    private static String TAG = "ServiceActivity";
    private static final String SERVICE_TYPE = "_socialiot._tcp";

    private final Context context;

    private NsdManager.DiscoveryListener discoveryListener = new NsdManager.DiscoveryListener() {

        // Called as soon as service discovery begins.
        @Override
        public void onDiscoveryStarted(String regType) {
            Log.d(TAG, "Service discovery started");
        }

        @Override
        public void onServiceFound(NsdServiceInfo service) {
            // A service was found! Do something with it.
            Log.d(TAG, "Service discovery success " + service);

            nsdManager.resolveService(service, new NsdManager.ResolveListener() {
                @Override
                public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                }

                @Override
                public void onServiceResolved(NsdServiceInfo serviceInfo) {
                    Log.d(TAG, "Service resolved " + serviceInfo);

                    int port = serviceInfo.getPort();
                    String host = serviceInfo.getHost().getHostAddress();

                    String address = "http://" + host + ":" + port + "/";
                    Log.d(TAG, "Host address is " + host + " port is " + port);
                    Log.d(TAG, "Address: " + address);

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("serverAddress", address);

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onServiceLost(NsdServiceInfo service) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            Log.e(TAG, "service lost: " + service);
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.i(TAG, "Discovery stopped: " + serviceType);
        }

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            nsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            nsdManager.stopServiceDiscovery(this);
        }
    };

    NsdHelper(Context context) {
        this.context = context;
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);

        Log.d(TAG, "NsdHelper()");
    }

    void discoverServices() {
        Log.d(TAG, "discoverServices()");
        nsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    public void stopDiscovery() {
        Log.d(TAG, "stopDiscovery()");
        nsdManager.stopServiceDiscovery(discoveryListener);
    }
}
