package org.lolhens.network;

/**
 * Created by LolHens on 28.12.2014.
 */
public interface IReceiveHandler<P> {
    public void onReceive(AbstractClient<P> client, P packet);
}
