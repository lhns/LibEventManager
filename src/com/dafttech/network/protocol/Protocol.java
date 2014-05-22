package com.dafttech.network.protocol;

import java.io.IOException;
import java.io.InputStream;

public abstract class Protocol {
    public abstract void read(InputStream inputStream) throws IOException;
}
