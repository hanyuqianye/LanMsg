package org.qianye.lanmsg.msg;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author qianye
 */
public class SimpleMsgPacket implements MsgPacket {

	// 包总长度
	private int length = DEFAULT_MSG_HEADER_LENGTH;
	
	// 包序号
	private long id=-1;
	
	// 包命令
	private int command = Command.INVALID_MSG;
	
	// 包 body
	private byte[] data;
	
	// 目标 Address
	private MsgAddress destAddress;

	public SimpleMsgPacket() {

	}

	/**
	 * 解析消息头
	 *
	 * @param data
	 *            字节数组
	 */
	public SimpleMsgPacket(byte[] data) {
		decode(data);
	}

	/**
	 * @param buff
	 * @param destAddress
	 */
	public SimpleMsgPacket(byte[] buff, MsgAddress destAddress) {
		this(buff);
		this.destAddress = destAddress;
	}

	private void decode(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		byte[] magic=new byte[MsgPacket.MSG_MAGIC_LENGTH];
		buffer.get(magic, 0, MsgPacket.MSG_MAGIC_LENGTH);
		length = buffer.getInt();
		id = buffer.getLong();
		command = buffer.getInt();
		int dataSize = length - DEFAULT_MSG_HEADER_LENGTH;
		if (dataSize > 0) {
			data = new byte[dataSize];
			buffer.get(data, 0, data.length);
		}
	}

	/**
	 * 打包
	 */
	@Override
	public byte[] encoded() {
		byte[] result;
		if (data != null) {
			length = DEFAULT_MSG_HEADER_LENGTH + data.length;
			ByteBuffer buff = ByteBuffer.allocate(length);
			buff.put(MSG_CMD_MAGIC, 0, MSG_MAGIC_LENGTH);
			buff.putInt(length);
			buff.putLong(id);
			buff.putInt(command);
			buff.put(data, 0, data.length);
			result = buff.array();
		} else {
			length = DEFAULT_MSG_HEADER_LENGTH;
			ByteBuffer buff = ByteBuffer.allocate(length);
			buff.put(MSG_CMD_MAGIC, 0, MSG_MAGIC_LENGTH);
			buff.putInt(length);
			buff.putLong(id);
			buff.putInt(command);
			result = buff.array();
		}
		return result;
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long packedId) {
		this.id = packedId;
	}

	@Override
	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public byte[] getData() {
		return this.data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public MsgAddress getDestAddress() {
		return destAddress;
	}

	public void setDestAddress(MsgAddress destAddr) {
		this.destAddress = destAddr;
	}

	@Override
	public String toString() {
		return getClass().getName() + "{" + "id=" + id + ", command=" + command
				+ ", length=" + length + ", data=" + Arrays.toString(data)
				+ ", destAddress=" + destAddress + '}';
	}
}
