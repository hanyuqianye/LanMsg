package org.qianye.lanmsg.factory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.qianye.lanmsg.msg.MsgAddress;
import org.qianye.lanmsg.msg.MsgPacket;

/**
 * @author Administrator
 */
public class MsgSenderFactory {

    /**
     * @param socket 数据报套接字
     * @param packet 数据包
     */
    public static void sendPacket(DatagramSocket socket, MsgPacket packet) {
        byte data[] = packet.encoded();
        MsgAddress dest = packet.getDestAddress();
        DatagramPacket dpack = new DatagramPacket(data, data.length, dest.getAddress(), dest.getPort());
        try {
            socket.send(dpack);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
