package com.fullwall.Citizens;

import java.util.concurrent.ConcurrentHashMap;

public class ActionManager {
	public static ConcurrentHashMap<Integer, ConcurrentHashMap<String, CachedAction>> actions = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, CachedAction>>();

	public static void putAction(int entityID, String name, CachedAction cached) {
		validateAction(entityID);
		actions.get(entityID).put(name, cached);
	}

	public static CachedAction getAction(int entityID, String name) {
		validateAction(entityID);
		return actions.get(entityID).get(name);
	}

	public static void validateAction(int entityID) {
		if (actions.get(entityID) == null)
			actions.put(entityID, new ConcurrentHashMap<String, CachedAction>());
	}

	public static void resetAction(int entityID, String name, String string,
			boolean bool) {
		CachedAction cached = getAction(entityID, name);
		if (cached.has(string) && bool) {
			cached.reset(string);
			putAction(entityID, name, cached);
		}
	}
}
