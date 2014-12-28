package org.lolhens.network;

/**
 * Created by LolHens on 28.12.2014.
 */
public interface IAcceptHandler<P> {
    public void onAccept(AbstractClient<P> client);
}
