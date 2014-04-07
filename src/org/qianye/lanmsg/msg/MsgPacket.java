package org.qianye.lanmsg.msg;

/**
 * @author longyu
 */
public interface MsgPacket {
	/**
 * 
 */
	final byte[] MSG_CMD_MAGIC = "long".getBytes();
	/**
	 * 
	 */
	final int MSG_MAGIC_LENGTH = 4;
	/**
	 * magic length id cmd data
	 */
	final int DEFAULT_MSG_HEADER_LENGTH = (2 + 2 + 2) + 4 + 8 + 4 + 4;
	/**
     *
     */
	final int DEFAULT_MSG_DATA_MAX_LENGTH = 63508;
	/**
     *
     */
	final int MAX_BUF = DEFAULT_MSG_HEADER_LENGTH + DEFAULT_MSG_DATA_MAX_LENGTH;

	/**
	 * 获取包的唯一序列号
	 * 
	 * @return 包的唯一编号
	 */
	public long getId();

	/**
	 * 获取包的cmd
	 * 
	 * @return 包的cmd
	 */
	public int getCommand();
	/**
	 * 获取包的总长度
	 * 
	 * @return 包的长度
	 */
	public int getLength();
	/**
	 * @return 包数据
	 */
	public byte[] getData();

	/**
	 * 获取发送方/接受方地址
	 * 
	 * @return 消息地址
	 */
	public MsgAddress getDestAddress();

	/**
	 * 组包
	 * 
	 * @return 组包后的数据
	 */
	public byte[] encoded();

	/**
     *
     */
	public class Command {

		/**
		 * 不合法
		 */
		public static final int INVALID_MSG = -1;
		/**
		 * 发送
		 */
		public static final int NEW_MESSAGE = 0;
		/**
		 * 回复
		 */
		public static final int REPLY_MSG = 1;
	}

}