package org.lolhens.network;

/**
 * Created by LolHens on 28.12.2014.
 */
public interface IHandlerReceive<P> {
    public void onReceive(AbstractClient<P> client, P packet);
}
