package org.lolhens.network;

/**
 * Created by LolHens on 28.12.2014.
 */
public interface IConnectHandler<P> {
    public void onConnect(AbstractClient<P> client);
}
