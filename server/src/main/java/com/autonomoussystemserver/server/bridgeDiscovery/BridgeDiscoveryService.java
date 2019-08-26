package com.autonomoussystemserver.server.bridgeDiscovery;

import retrofit2.http.GET;

import java.util.List;

public interface BridgeDiscoveryService {
    @GET("nupnp")
    public List<DiscoveredBridge> discoverBridges();
}
