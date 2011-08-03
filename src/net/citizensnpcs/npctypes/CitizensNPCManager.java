package net.citizensnpcs.npctypes;

import java.util.Map;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.properties.PropertyManager;

import com.google.common.collect.Maps;

public class CitizensNPCManager {
	private static final Map<String, CitizensNPC> types = Maps.newHashMap();

	public static CitizensNPC registerType(CitizensNPC type) {
		types.put(type.getType(), type);
		PropertyManager.add(type.getType(), type.getProperties());
		Citizens.commands.register(type.getCommands().getClass());
		return type;
	}

	public static boolean validType(String type) {
		return types.get(type) != null;
	}

	public static CitizensNPC getType(String type) {
		return types.get(type);
	}

	public static Map<String, CitizensNPC> getTypes() {
		return types;
	}
}