package org.lolhens.state;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by LolHens on 13.01.2015.
 */
public class StateLock {
    private Set<Object> locks = new HashSet<>();
    private StateListener listener;

    public StateLock(StateListener listener) {
        this.listener = listener;
    }

    public void setEnabled(Object key, boolean value) {
        if (value) {
            locks.remove(key);
            if (locks.size() == 0) listener.onEnable(true);
        } else {
            if (locks.size() == 0) listener.onEnable(false);
            locks.add(key);
        }
    }
}
