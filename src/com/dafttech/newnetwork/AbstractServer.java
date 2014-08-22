package com.dafttech.newnetwork;

import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

public class AbstractServer<P extends Packet> extends ProtocolProvider<P> {

    public AbstractServer(Protocol<P> protocol) {
        super(protocol);
    }
}
