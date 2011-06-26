package com.temp.Misc;

import java.util.concurrent.ConcurrentHashMap;

import com.temp.Misc.CachedAction;

public class ActionManager {
	private static final ConcurrentHashMap<Integer, ConcurrentHashMap<String, CachedAction>> actions = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, CachedAction>>();

	public static void putAction(int entityID, String name, CachedAction cached) {
		validateAction(entityID);
		actions.get(entityID).put(name, cached);
	}

	public static CachedAction getAction(int entityID, String name) {
		validateAction(entityID);
		validateCache(entityID, name);
		return actions.get(entityID).get(name);
	}

	private static void validateCache(int entityID, String name) {
		if (actions.get(entityID).get(name) == null) {
			actions.get(entityID).put(name, new CachedAction());
		}
	}

	private static void validateAction(int entityID) {
		if (actions.get(entityID) == null) {
			actions.put(entityID, new ConcurrentHashMap<String, CachedAction>());
		}
	}

	public static void resetAction(int entityID, String name, String string,
			boolean bool) {
		if (bool) {
			resetAction(entityID, name, string);
		}
	}

	public static void resetAction(int entityID, String name, String string) {
		CachedAction cached = getAction(entityID, name);
		if (cached.has(string)) {
			cached.reset(string);
			putAction(entityID, name, cached);
		}
	}
}