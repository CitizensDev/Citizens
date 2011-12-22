package net.citizensnpcs.npctypes;

import java.util.Map;

import net.citizensnpcs.Citizens;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Listener;

import com.google.common.collect.Maps;

public class NPCTypeManager {
	private static final Map<String, CitizensNPCType> types = Maps.newHashMap();

	public static CitizensNPCType registerType(CitizensNPCType type) {
		types.put(type.getName(), type);
		Citizens.commands.register(type.getCommands().getClass());
		return type;
	}

	public static boolean validType(String type) {
		return types.get(type) != null;
	}

	public static CitizensNPCType getType(String type) {
		return types.get(type);
	}

	public static Map<String, CitizensNPCType> getTypes() {
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