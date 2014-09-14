package com.dafttech.network.disconnect;

import java.io.EOFException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public abstract class Disconnect {
    @Override
    public abstract String toString();

    public static final Disconnect fromException(Exception e) {
        if (e instanceof EOFException) {
            return new EOF();
        } else if (e instanceof SocketException && e.getMessage().startsWith("Connection reset")) {
            return new Reset();
        } else if (e instanceof SocketTimeoutException) {
            return new Timeout();
        }
        return new Unknown();
    }
}
