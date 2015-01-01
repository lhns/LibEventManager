package org.lolhens.network;

import java.nio.channels.SelectionKey;

/**
 * Created by LolHens on 01.01.2015.
 */
public interface IHandlerSelect {
    public int onSelect(SelectionKey selectionKey, int readyOps);
}
