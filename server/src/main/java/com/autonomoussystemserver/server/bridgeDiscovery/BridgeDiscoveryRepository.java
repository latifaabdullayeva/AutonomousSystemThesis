package com.autonomoussystemserver.server.bridgeDiscovery;

import org.springframework.lang.NonNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class BridgeDiscoveryRepository implements BridgeDiscovery {
    /*
    Retrieving local hue IP address from https://discovery.meethue.com webpage,
    it will result in a list of connected bridges in the network
    In case of an empty JSON array reply (i.e. [ ]), no Hue bridge has been found.
    This can be because the user never connected the bridge to the Internet
    or it has been considered to be disconnected, in that case an option is to perform an “IP scan”,
    or ask the user to enter an IP address of the Hue bridge.
    */

    private String discoveryHueURL = "https://www.meethue.com/api/"; // or https://www.meethue.com/api/nupnp or https://discovery.meethue.com

    void setDiscoveryHueURL(String discoveryHueURL) {
        this.discoveryHueURL = discoveryHueURL;
    }

    @Override
    public List<DiscoveredBridge> discover() {
        return discoverBridges();
    }

    @NonNull
    private List<DiscoveredBridge> discoverBridges() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(discoveryHueURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BridgeDiscoveryService bridgeDiscoveryService = retrofit.create(BridgeDiscoveryService.class);
        return bridgeDiscoveryService.discoverBridges();
    }
}