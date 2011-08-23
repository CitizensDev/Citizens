package net.citizensnpcs.api;

import net.citizensnpcs.SettingsManager.SettingsType;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.properties.properties.UtilityProperties;

/**
 * A Node of Citizens is a property value that can be generated in citizens.yml
 */
public class Node {
	private String name;
	private SettingsType type;
	private String path;
	private Object value;

	public Node(String name, SettingsType type, String path, Object value) {
		this.name = name;
		this.type = type;
		this.path = path;
		this.value = value;
	}

	public Storage getFile() {
		switch (this.getType()) {
		case GENERAL:
			return UtilityProperties.getSettings();
		case MOB:
			return UtilityProperties.getMobSettings();
		}
		return null;
	}

	/**
	 * Used for identification, this should never need to be changed, unlike the
	 * path
	 * 
	 * @return name Name of a node
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the type of the node, which determines which file the node is written
	 * to
	 * 
	 * @return type Type of a node
	 */
	public SettingsType getType() {
		return this.type;
	}

	/**
	 * Get the path of the node
	 * 
	 * @return path Path of a node
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Get the value of the node
	 * 
	 * @return value Value of a node
	 */
	public Object getValue() {
		return this.value;
	}

	public void set(Object value) {
		this.value = value;
	}
}