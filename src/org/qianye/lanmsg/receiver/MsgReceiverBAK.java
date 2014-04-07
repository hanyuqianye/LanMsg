package org.qianye.lanmsg.receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.qianye.lanmsg.event.MsgEvent;
import org.qianye.lanmsg.event.MsgEventListener;
import org.qianye.lanmsg.msg.MsgAddress;
import org.qianye.lanmsg.msg.MsgPacket;
import org.qianye.lanmsg.msg.SimpleMsgPacket;

/**
 * 消息接收线程
 *
 * @author dragon
 */
public class MsgReceiverBAK extends MsgEventListener implements Runnable {

	private final DatagramSocket socket;

	private Thread thread = null;

	/**
	 * @param socket
	 *            DatagramSocket
	 */
	public MsgReceiverBAK(DatagramSocket socket) {
		this.socket = socket;
	}

	public synchronized void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.setName(getClass().getName());
			thread.start();
		}
	}
	/**
	 * 
	 */
	public synchronized void stop() {
		if (thread != null) {
			Thread tmp = thread;
			thread = null;
			if (tmp.isAlive()) {
				tmp.interrupt();
				wakeup();
				try {
					tmp.join(300);//for cleanup
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				System.out.println("jja");
			}
		}
	}
	/**
	 * 唤醒阻塞的IO
	 */
	private void wakeup() {
		byte[] mess = "stop".getBytes();
		try {
			InetSocketAddress add = new InetSocketAddress(InetAddress.getLocalHost(), socket.getLocalPort());
			DatagramPacket p = new DatagramPacket(mess, mess.length, add);
			socket.send(p);
		} catch (IOException ie) {
			throw new RuntimeException(" thread" + thread.getName() + ":" + ie);
		}
	}

	@Override
	public void run() {
		final byte[] buffer = new byte[MsgPacket.MAX_BUF];
		final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		while (thread != null && thread.isAlive()) {
			try {
				socket.receive(packet);
				parsMsg(packet);
			} catch (IOException ie) {
				throw new RuntimeException(ie);
			}
		}
		System.out.println("--------------------------thread exit ok------------------------------------!");
	}

	/**
	 * @param datagramPacket
	 */
	private void parsMsg(DatagramPacket datagramPacket) {
		int port = datagramPacket.getPort();
		InetAddress remoteAddress = datagramPacket.getAddress();
		byte[] bytes = datagramPacket.getData();
		MsgAddress msgAddress = new MsgAddress(remoteAddress, port);
		MsgPacket msgPacket = new SimpleMsgPacket(bytes, msgAddress);
		fire(MsgEvent.RECEIVED_MSG, msgPacket);
	}
}
