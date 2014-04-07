package org.qianye.lanmsg.test.demo;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Hashtable;

import org.qianye.lanmsg.event.MsgEvent;
import org.qianye.lanmsg.factory.MsgPacketFactory;
import org.qianye.lanmsg.factory.MsgSenderFactory;
import org.qianye.lanmsg.msg.MsgAddress;
import org.qianye.lanmsg.msg.MsgPacket;
import org.qianye.lanmsg.msg.SimpleMsgPacket;
import org.qianye.lanmsg.receiver.MsgReceiver;
import org.qianye.lanmsg.sender.MsgSender;


/**
 * @author Administrator
 */
public class SenderTest {

    private final Hashtable<Long, MsgSender> senders = new Hashtable<>();
    
    private DatagramSocket socket;

    public SenderTest() {
        try {
            socket = new DatagramSocket(1234);
            receive();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        InetAddress remoteAddress = InetAddress.getByName("localhost");
        MsgAddress destAddress = new MsgAddress(remoteAddress, 1234);
        SenderTest sender = new SenderTest();
        String message = "this is simple message from localhost";
        //while (true) {
            System.out.println("发送队列："+sender.senders);
            SimpleMsgPacket msgPacket = MsgPacketFactory.createSendMsg(message.getBytes(), destAddress);
            sender.send(msgPacket);
//            try {
//                Thread.sleep(50000);
//            } catch (InterruptedException e) {
//            }
        //}
    }

    private void send(MsgPacket msgPacket) {
        final MsgSender normal = new MsgSender(socket, msgPacket);
        normal.on(MsgEvent.MSG_SEND_OK, (event) -> {
            MsgPacket packet = (MsgPacket) event.getArgs()[0];
            long key = packet.getId();
            System.out.println("发包:" + key + " 成功！");
            System.out.println("发送队列："+senders);
        });

        normal.on(MsgEvent.MSG_SEND_FAIL, (event) -> {
            MsgPacket packet = (MsgPacket) event.getArgs()[0];
            long key = packet.getId();
            senders.remove(key);
            System.err.println("丢包:" + packet.toString());
        });

        long key = msgPacket.getId();
        senders.put(key, normal);
        normal.send();
    }

    private void receive() {
        MsgReceiver receive = new MsgReceiver(socket);
        receive.on(MsgEvent.RECEIVED_MSG, (event) -> {
            MsgPacket msgPacket = (MsgPacket) event.getArgs()[0];
            switch (msgPacket.getCommand()) {
                case MsgPacket.Command.NEW_MESSAGE:
                    System.out.println("接受到新包！");
                    if (msgPacket.getData() != null) {
                        System.out.println("msg body==>" + new String(msgPacket.getData()));
                    }

                    MsgPacket replyMsg = MsgPacketFactory.createReplyMsg(msgPacket);
                    MsgSenderFactory.sendPacket(socket, replyMsg);
                    break;

                case MsgPacket.Command.REPLY_MSG:
                    System.out.println("接受到回复包！");
                    long key = msgPacket.getId();
                    MsgSender sender = senders.get(key);
                    if (sender != null) {
                        sender.notifyReply();
                        senders.remove(key);
                        receive.stop();
                    }
                    break;

                default:
                    System.err.println("接受到非法消息");
                    break;
            }
        });
        receive.start();
    }
}
