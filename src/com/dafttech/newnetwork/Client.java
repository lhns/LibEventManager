package com.dafttech.newnetwork;

import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;

public class Client<P extends Packet> extends NetworkInterface<P> {
    public Client(Protocol<P> protocol) {
        super(protocol);
    }

}
