package net.citizensnpcs.misc;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public class Actions {
	private final SetMultimap<String, String> actions = HashMultimap.create();

	public void clear(String action, String to) {
		actions.remove(action.toLowerCase(), to.toLowerCase());
	}

	public void clear(String action, String to, boolean bool) {
		if (bool)
			clear(action, to);
	}

	public boolean has(String action, String to) {
		return actions.containsEntry(action.toLowerCase(), to.toLowerCase());
	}

	public void set(String action, String to) {
		actions.put(action.toLowerCase(), to.toLowerCase());
	}
}