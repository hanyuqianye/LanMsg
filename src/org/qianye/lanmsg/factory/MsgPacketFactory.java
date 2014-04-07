package org.qianye.lanmsg.factory;

import org.qianye.lanmsg.msg.MsgAddress;
import org.qianye.lanmsg.msg.MsgPacket;
import org.qianye.lanmsg.msg.SimpleMsgPacket;

public class MsgPacketFactory {
    /**
     * @param src
     * @return SimpleMsgPacket
     */
    public static SimpleMsgPacket createReplyMsg(final MsgPacket src) {
        SimpleMsgPacket packet = new SimpleMsgPacket();
        packet.setId(src.getId());
        packet.setCommand(MsgPacket.Command.REPLY_MSG);
        packet.setDestAddress(src.getDestAddress());
        return packet;
    }

    /**
     * @return SimpleMsgPacket
     */
    public static SimpleMsgPacket createSendMsg() {
        SimpleMsgPacket packet = new SimpleMsgPacket();
        packet.setId(System.currentTimeMillis());
        packet.setCommand(MsgPacket.Command.NEW_MESSAGE);
        return packet;
    }

    /**
     * @param data
     * @param msgAddress
     * @return SimpleMsgPacket
     */
    public static SimpleMsgPacket createSendMsg(byte[] data, MsgAddress msgAddress) {
    	if(data.length>MsgPacket.DEFAULT_MSG_DATA_MAX_LENGTH) throw new RuntimeException(" data.length>MsgPacket.DEFAULT_MSG_DATA_MAX_LENGTH");
    	System.out.println("data.length="+data.length);
        SimpleMsgPacket packet = new SimpleMsgPacket();
        packet.setId(System.currentTimeMillis());
        packet.setCommand(MsgPacket.Command.NEW_MESSAGE);
        packet.setData(data);
        packet.setDestAddress(msgAddress);
        return packet;
    }
}
