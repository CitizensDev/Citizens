package net.citizensnpcs.properties;

import net.citizensnpcs.SettingsManager.SettingsType;
import net.citizensnpcs.properties.properties.UtilityProperties;

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

	/**
	 * Helper method that uses a node's type to determine which file it should
	 * be written to
	 * 
	 * @return file a node is written to
	 */
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

	/**
	 * Used for identification, this should never need to be changed, unlike the
	 * path
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the type of the node, which determines which file the node is written
	 * to
	 * 
	 * @return type
	 */
	public SettingsType getType() {
		return this.type;
	}

	/**
	 * Get the path of the node
	 * 
	 * @return path
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Get the value of the node
	 * 
	 * @return value
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * Set the value of the node
	 * 
	 * @param value
	 */
	public void set(Object value) {
		this.value = value;
	}
}