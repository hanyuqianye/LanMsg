package org.qianye.lanmsg.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MsgEventListener implements EventListener {

	private final Map<Integer, EventHandler> actionMap = new ConcurrentHashMap<>();

	@Override
	final public void on(int actionType, EventHandler handler) {
		if (!actionMap.containsKey(actionType)) {
			actionMap.put(actionType, handler);
		}
	}

	@Override
	final public void remove(int actionType) {
		if (actionMap.containsKey(actionType)) {
			actionMap.remove(actionType);
		}
	}

	@Override
	final public void fire(int actionType, Object... args) {
		if (actionMap.containsKey(actionType)) {
			EventHandler listener = actionMap.get(actionType);
			if (null != listener) {
				listener.handler(new EventObject(getClass(), args));
			}
		}
	}
}
