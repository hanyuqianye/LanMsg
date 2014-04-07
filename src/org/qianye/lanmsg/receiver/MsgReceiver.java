package org.qianye.lanmsg.receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

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
public class MsgReceiver extends MsgEventListener implements Runnable {

	private final DatagramSocket socket;

	private Thread thread = null;

	//Poison pill
	/**
	 * @param socket
	 *            DatagramSocket
	 */
	public MsgReceiver(DatagramSocket socket) {
		this.socket = socket;
	}

	/**
	 * 
	 */

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
			wakeup();
			// cleanup
			thread = null;
		}
	}
	/**
	 * 通过"毒丸"唤醒阻塞的IO
	 */
	private void wakeup() {
		byte[] mess = new byte[]{'s', 't', 'o', 'p'};
		try {
			InetSocketAddress localAddrees = new InetSocketAddress(InetAddress.getLocalHost(), socket.getLocalPort());
			DatagramPacket packet = new DatagramPacket(mess, mess.length, localAddrees);
			socket.send(packet);
		} catch (IOException ie) {
			throw new RuntimeException(" thread" + thread.getName() + ":" + ie);
		}
	}

	@Override
	public void run() {
		final byte[] buffer = new byte[MsgPacket.MAX_BUF];
		final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		while (true) {
			try {
				socket.receive(packet);
				byte[] bytes = packet.getData();
				byte[] magic=new byte[MsgPacket.MSG_MAGIC_LENGTH];
				System.arraycopy(bytes,0, magic, 0, 4);
                /* check magic */
				if(Arrays.equals(magic,MsgPacket.MSG_CMD_MAGIC))
				{
					parsMsg(packet);
				}
				else if (Arrays.equals(magic,new byte[]{'s', 't', 'o', 'p'})) {
					System.out.println(new String(bytes));
					break;
				}
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