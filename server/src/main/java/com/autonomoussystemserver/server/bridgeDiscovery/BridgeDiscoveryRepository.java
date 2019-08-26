package com.autonomoussystemserver.server.bridgeDiscovery;

import org.springframework.lang.NonNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class BridgeDiscoveryImplNupnp implements BridgeDiscovery {

    private String nupnpUrl = "https://www.meethue.com/api/";

    void setNupnpUrl(String nupnpUrl) {
        this.nupnpUrl = nupnpUrl;
    }

    @Override
    public List<DiscoveredBridge> discover() {
        return discoverBridges();
    }

    @NonNull
    private List<DiscoveredBridge> discoverBridges() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(nupnpUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BridgeDiscoveryApi api = retrofit.create(BridgeDiscoveryApi.class);
        return api.discoverBridges();
    }
}
