package com.dafttech.network;

import java.io.IOException;

public interface INetworkInterface {
    public int read() throws IOException;

    public byte[] read(byte[] array) throws IOException;

    public void write(byte... data) throws IOException;
}
