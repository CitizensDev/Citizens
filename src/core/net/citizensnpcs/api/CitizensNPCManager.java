package net.citizensnpcs.api;

import java.util.Map;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Listener;

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

	/**
	 * Helper method used to register events per-type
	 * 
	 * @param eventType
	 *            Bukkit Event.Type
	 * @param listener
	 *            Bukkit Listener interface
	 * @param priority
	 *            Bukkit Event.Priority
	 */
	public static void registerEvent(Type eventType, Listener listener,
			Priority priority) {
		Citizens.plugin.getServer().getPluginManager()
				.registerEvent(eventType, listener, priority, Citizens.plugin);
	}

	/**
	 * Helper method used to register events per-type with default "Normal"
	 * priority
	 * 
	 * @param eventType
	 *            Bukkit Event.Type
	 * @param listener
	 *            Bukkit Listener interface
	 */
	public static void registerEvent(Type eventType, Listener listener) {
		registerEvent(eventType, listener, Priority.Normal);
	}
}