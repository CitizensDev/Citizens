package com.citizens.Properties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SettingsTree {
	private final Map<String, String> tree = new ConcurrentHashMap<String, String>();

	public void populate(String path) {
		StringBuilder progressive = new StringBuilder();
		int index = 0;
		String[] branches = path.split("\\.");
		for (String branch : branches) {
			progressive.append(branch);
			if (getTree().get(progressive.toString()) == null) {
				getTree().put(progressive.toString(), progressive.toString());
			}
			if (index != branches.length - 1) {
				progressive.append(".");
			}
			++index;
		}
	}

	public String get(String path) {
		return getTree().get(path);
	}

	public Map<String, String> getTree() {
		return tree;
	}

	public void set(String path, String value) {
		populate(path);
		tree.put(path, value);
	}

	public void remove(String path) {
		for (String key : tree.keySet()) {
			if (key.startsWith(path))
				tree.remove(key);
		}
	}
}