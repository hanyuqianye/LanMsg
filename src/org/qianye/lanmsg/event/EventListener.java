package org.qianye.lanmsg.event;

/**
 * @author Administrator
 */
public interface EventListener {

	/**
	 * @param eventType
	 * @param handler
	 */
	public void on(int eventType, EventHandler handler);

	/**
	 * @param eventType
	 */
	public void remove(int eventType);
	/**
	 * 
	 * @param actionType
	 * @param args
	 */
	void fire(int actionType, Object... args);

}
