package com.citizens;

import com.citizens.SettingsManager.SettingsType;

public class Node {
	private SettingsType type;
	private String path;
	private Object value;

	public Node(SettingsType type, String path, Object value) {
		this.type = type;
		this.path = path;
		this.value = value;
	}

	public SettingsType getType() {
		return this.type;
	}

	public String getPath() {
		return this.path;
	}

	public Object getValue() {
		return this.value;
	}

	public void set(Object value) {
		this.value = value;
	}
}