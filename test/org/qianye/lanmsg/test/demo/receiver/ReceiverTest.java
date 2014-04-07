package org.qianye.lanmsg.test.demo.receiver;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.qianye.lanmsg.receiver.MsgReceiverBAK;

public class ReceiverTest {
	public static void main(String[] args) throws SocketException,
			InterruptedException, UnknownHostException {

		DatagramSocket socket = new DatagramSocket(1234);

		MsgReceiverBAK receive = new MsgReceiverBAK(socket);
		receive.stop();
		//

		new Thread("test thread_" + 1) {
			public void run() {
				int i = 0;
				while (i < 1000) {
					System.out.println(Thread.currentThread().getName() + "_"
							+ i);
					receive.start();

					System.out.println("Asking thread to stop...");
					receive.stop();
					System.out.println("Stopping application ok!");
					i++;
				}
			}
		}.start();

		new Thread("test thread_" + 2) {
			public void run() {
				int i = 0;
				while (i < 1000) {
					System.out.println(Thread.currentThread().getName() + "_"
							+ i);
					System.out.println("Asking thread to stop...");
					receive.stop();

					receive.start();

					System.out.println("Stopping application ok!");
					i++;
				}
			}
		}.start();;

		new Thread("test thread_" + 3) {
			public void run() {
				int i = 0;
				while (i < 1000) {
					System.out.println(Thread.currentThread().getName() + "_"
							+ i);
					System.out.println("Asking thread to stop...");
					receive.stop();

					receive.start();

					System.out.println("Asking thread to stop...");
					receive.stop();

					System.out.println("Stopping application ok!");
					i++;
				}
			}
		}.start();;
	}
}
