package org.qianye.lanmsg.test.demo.receiver;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.qianye.lanmsg.receiver.MsgReceiver;

public class ReceiverTestOne {
	public static void main(String[] args) throws SocketException,
			InterruptedException, UnknownHostException {

		DatagramSocket socket = new DatagramSocket(1234);

		MsgReceiver receive = new MsgReceiver(socket);
		receive.stop();
		for (int i = 0; i < 5000; i++) {
			new Thread() {
				public void run() {
					receive.start();
					System.out.println("Asking thread to stop...");
					receive.stop();
					System.out.println("Stopping application ok!");
				}
			}.start();;
			
			new Thread() {
				public void run() {
					receive.stop();
					System.out.println("Asking thread to stop...");
					receive.start();
					System.out.println("Stopping application ok!");
				}
			}.start();;
		}
	}
}
