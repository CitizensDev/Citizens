package net.citizensnpcs.npctypes;

import java.util.Map;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.properties.PropertyManager;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.google.common.collect.Maps;

public class NPCTypeManager {
    private static final Map<String, CitizensNPCType> types = Maps.newHashMap();

    public static CitizensNPCType registerType(CitizensNPCType type) {
        types.put(type.getName(), type);
        PropertyManager.add(type.getName(), type.getProperties());
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

    public static void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, Citizens.plugin);
    }
}