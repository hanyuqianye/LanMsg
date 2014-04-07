package org.qianye.lanmsg.event;


public class MsgEvent extends EventObject {

    /**
     * 消息发送成功
     */
    public final static int MSG_SEND_OK = 100;

    /**
     * 消息发送失败
     */
    public final static int MSG_SEND_FAIL = 101;

    /**
     * 接受到消息
     */
    public final static int RECEIVED_MSG = 200;

    public MsgEvent(Object source, Object[] args) {
        super(source, args);
    }

}
