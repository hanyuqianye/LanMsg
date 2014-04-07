package org.qianye.lanmsg.sender;

import org.qianye.lanmsg.event.MsgEvent;
import org.qianye.lanmsg.event.MsgEventListener;
import org.qianye.lanmsg.msg.MsgAddress;
import org.qianye.lanmsg.msg.MsgPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * MsgSender
 */
public class MsgSender extends MsgEventListener implements Runnable {

    /**
     * 重发间隔时间
     */
    private static final int WAIT = 100;
    /**
     * 重复次数
     */
    private static final int RETRY = 3;
    /**
     *
     */
    private final DatagramSocket socket;
    /**
     *
     */
    private final MsgPacket msgPacket;
    /**
     * 回复标示
     */
    private volatile boolean reply = false;

    /**
     * @param socket        数据报套接字
     * @param packet 数据包
     */
    public MsgSender(DatagramSocket socket, MsgPacket packet) {
        this.socket = socket;
        this.msgPacket = packet;
    }

    /**
     * @param socket
     *            数据报套接字
     * @param msgPacketImpl
     *            数据包
     */
    /**
     * 发送数据报
     */
    void sendPacket() {
        byte bytes[] = msgPacket.encoded();
        MsgAddress dest = msgPacket.getDestAddress();
        DatagramPacket dpack = new DatagramPacket(bytes, bytes.length,dest.getAddress(), dest.getPort());
        try {
            socket.send(dpack);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        for (int i = 1; i <= RETRY; i++) {
            sendPacket();
            try {
                Thread.sleep(WAIT);
            } catch (InterruptedException e) {
            }
            if (reply) {
                fire(MsgEvent.MSG_SEND_OK, msgPacket);
                return;
            }
        }
        fire(MsgEvent.MSG_SEND_FAIL, msgPacket);
    }

    /**
     * 接受到回复
     */
    public void notifyReply() {
        reply = true;
    }

    /**
     * 发送消息<br>
     * 调用该方法，采用线程发送消息
     */
    public void send() {
        Thread thread = new Thread(this);
        thread.setName(MsgSender.class.getName() + msgPacket.getId());
        thread.start();
    }

}
