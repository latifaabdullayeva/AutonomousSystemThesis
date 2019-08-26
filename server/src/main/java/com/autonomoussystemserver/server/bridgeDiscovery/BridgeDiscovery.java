package com.autonomoussystemserver.server.bridgeDiscovery;


import java.util.List;

public interface BridgeDiscovery {
    public List<DiscoveredBridge> discover();
}