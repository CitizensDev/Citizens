package com.citizens.properties;

import com.citizens.interfaces.Storage;
import com.citizens.properties.SettingsManager.SettingsType;
import com.citizens.properties.properties.UtilityProperties;

public class Node {
	private SettingsType type;
	private String path;
	private Object value;

	public Node(SettingsType type, String path, Object value) {
		this.type = type;
		this.path = path;
		this.value = value;
	}

	public Storage getFile() {
		switch (this.getType()) {
		case GENERAL:
			return UtilityProperties.getSettings();
		case ECONOMY:
			return UtilityProperties.getEconomySettings();
		case MOB:
			return UtilityProperties.getMobSettings();
		}
		return null;
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