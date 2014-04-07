package org.qianye.lanmsg.event.test;

import org.qianye.lanmsg.event.EventHandler;
import org.qianye.lanmsg.event.EventObject;
import org.qianye.lanmsg.event.MsgEventListener;

public class MsgEventListenerTest {

	public static void main(String args[]) {
		MsgEventListener listener = new MsgEventListener();
		listener.on(0, event -> {
            System.out.println(event.getArgs()[0]);
        });
		listener.fire(0, "aa");
		listener.remove(0);

	}
}
