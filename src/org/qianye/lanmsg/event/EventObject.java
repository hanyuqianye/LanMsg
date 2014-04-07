package org.qianye.lanmsg.event;

public class EventObject {

	/**
	 * 可选参数
	 */
	protected Object args[];
	/**
	 * 事件源
     */
	protected transient Object source;

	public EventObject(Object source, Object eventArgs[]) {
		if (source == null)
			throw new IllegalArgumentException("null source");
		this.source = source;
		this.args = eventArgs;
	}

	public Object getSource() {
		return source;
	}

	public Object[] getArgs() {
		return args;
	}

}
