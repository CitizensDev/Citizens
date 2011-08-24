package net.citizensnpcs.npctypes;

import java.util.Map;

import org.bukkit.event.Listener;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.properties.PropertyManager;

import com.google.common.collect.Maps;

public class NPCTypeManager {

	private static final Map<String, CitizensNPC> types = Maps.newHashMap();

	public static CitizensNPC registerType(CitizensNPC type) {
		types.put(type.getName(), type);
		PropertyManager.add(type.getName(), type.getProperties());
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

	public static void registerEvent(Type eventType, Listener listener,
			Priority priority) {
		Citizens.plugin.getServer().getPluginManager()
				.registerEvent(eventType, listener, priority, Citizens.plugin);
	}

	public static void registerEvent(Type eventType, Listener listener) {
		registerEvent(eventType, listener, Priority.Normal);
	}
}