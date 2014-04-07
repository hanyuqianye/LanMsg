package org.qianye.lanmsg.test.demo;

import java.net.DatagramSocket;
import java.net.SocketException;

import org.qianye.lanmsg.event.MsgEvent;
import org.qianye.lanmsg.factory.MsgSenderFactory;
import org.qianye.lanmsg.msg.MsgPacket;
import org.qianye.lanmsg.msg.SimpleMsgPacket;
import org.qianye.lanmsg.receiver.MsgReceiverBAK;

public class RecverTest {

    private DatagramSocket socket;

    /**
     *
     */
    public RecverTest() {
        try {
            System.out.println("receive ...");
            socket = new DatagramSocket(5678);
            MsgReceiverBAK receive = new MsgReceiverBAK(socket);
            receive.on(MsgEvent.RECEIVED_MSG, event -> {
                SimpleMsgPacket packet = (SimpleMsgPacket) event.getArgs()[0];
                switch (packet.getCommand()) {
                    case MsgPacket.Command.NEW_MESSAGE:
                        SimpleMsgPacket replyPacket = new SimpleMsgPacket();
                        replyPacket.setDestAddress(packet.getDestAddress());
                        replyPacket.setCommand(MsgPacket.Command.REPLY_MSG);
                        replyPacket.setId(packet.getId());
                        System.out.println("----->接受到新消息" + packet.getId());
                        if(packet.getData()!=null){
                            System.out.println("msg body==>"+new String(packet.getData()));
                        }
                        MsgSenderFactory.sendPacket(socket, replyPacket);

                        break;
                    case MsgPacket.Command.REPLY_MSG:

                        break;
                    default:
                        System.err.println("接受到非法消息");
                        break;
                }
            });

            receive.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new RecverTest();
    }
}
