package org.qianye.lanmsg.msg;

import java.net.InetAddress;

public class MsgAddress {

    private int port;

    private InetAddress address;

    public MsgAddress(InetAddress address, int port) {
        this.port = port;
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{port=" + port +", address=" + address.getHostAddress() +'}';
    }
}
