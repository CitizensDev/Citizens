package com.citizens.NPCs;

import java.util.HashMap;
import java.util.Map;

import com.citizens.Interfaces.NPCFactory;
import com.citizens.Interfaces.NPCType;

public class NPCTypeManager {
	private static final Map<String, NPCType> types = new HashMap<String, NPCType>();

	public static NPCFactory getFactory(String string) {
		return getTypes().get(string).factory();
	}

	public static void registerType(NPCType type) {
		getTypes().put(type.getType(), type);
	}

	public static boolean validType(String type) {
		return getTypes().get(type) != null;
	}

	public static NPCType getType(String type) {
		return getTypes().get(type);
	}

	public static void registerType(NPCType type, boolean autosave) {
		if (autosave) {
			type.registerAutosave();
		}
		registerType(type);
	}

	public static Map<String, NPCType> getTypes() {
		return types;
	}
}