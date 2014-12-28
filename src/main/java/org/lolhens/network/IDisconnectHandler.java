package org.lolhens.network;

import org.lolhens.network.disconnect.DisconnectReason;

/**
 * Created by LolHens on 28.12.2014.
 */
public interface IDisconnectHandler<P> {
    public void onDisconnect(ProtocolProvider<P> protocolProvider, DisconnectReason disconnectReason);
}
