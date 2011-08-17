package net.citizensnpcs.npctypes;

import java.util.List;
import java.util.Map;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.listeners.Listener;
import net.citizensnpcs.properties.PropertyManager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CitizensNPCManager {
	private static final Map<String, CitizensNPC> types = Maps.newHashMap();
	private static final List<Listener> listeners = Lists.newArrayList();

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

	public static List<Listener> getListeners() {
		return listeners;
	}

	public static void addListener(Listener listener) {
		listeners.add(listener);
	}
}