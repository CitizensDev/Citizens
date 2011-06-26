package com.Citizens.Misc;

import java.util.HashMap;

public class CachedAction {
	private final HashMap<String, Boolean> actions = new HashMap<String, Boolean>();

	public boolean has(String string) {
		if (actions.get(string) == null) {
			actions.put(string, false);
		}
		return actions.get(string);
	}

	public void reset(String string) {
		actions.put(string, false);
	}

	public void set(String string) {
		actions.put(string, true);
	}
}