package com.citizens.npctypes;

import java.util.Map;

import com.citizens.Citizens;
import com.citizens.commands.CommandHandler;
import com.citizens.properties.PropertyManager;
import com.google.common.collect.Maps;

public class CitizensNPCManager {
	private static final Map<String, CitizensNPC> types = Maps.newHashMap();

	public static CitizensNPC registerType(CitizensNPC type) {
		types.put(type.getType(), type);
		return type;
	}

	public static boolean validType(String type) {
		return types.get(type) != null;
	}

	public static CitizensNPC getType(String type) {
		return types.get(type);
	}

	public static CitizensNPC registerType(CitizensNPC type, boolean autosave) {
		if (autosave) {
			PropertyManager.add(type.getType(), type.getProperties());
		}
		return registerType(type);
	}

	public static Map<String, CitizensNPC> getTypes() {
		return types;
	}

	public void registerCommands(Class<? extends CommandHandler> commands) {
		Citizens.commands.register(commands);
	}
}