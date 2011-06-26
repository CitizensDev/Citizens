package com.temp.Properties;

import java.util.HashMap;

import com.temp.Interfaces.Saveable;
import com.temp.NPCs.NPCManager;
import com.temp.Properties.ConfigurationHandler;
import com.temp.Properties.Properties.BasicProperties;
import com.temp.Properties.Properties.QuestProperties;
import com.temp.Properties.Properties.UtilityProperties;
import com.temp.resources.redecouverte.NPClib.HumanNPC;

public class PropertyManager {
	private static final HashMap<String, Saveable> properties = new HashMap<String, Saveable>();
	protected static final ConfigurationHandler profiles = new ConfigurationHandler(
			"plugins/Citizens/npc-profiles.yml");

	public enum PropertyType {
		BASIC,
		TRADER,
		HEALER,
		WIZARD,
		QUESTER,
		BLACKSMITH,
		GUARD;
	}

	public static void registerProperties() {
		add("basic", new BasicProperties());
		UtilityProperties.initialize();
		QuestProperties.initialize();
	}

	public static void add(String type, Saveable saveable) {
		properties.put(type, saveable);
	}

	public static ConfigurationHandler getNPCProfiles() {
		return profiles;
	}

	public static boolean npcHasType(HumanNPC npc, String type) {
		return profiles.pathExists(npc.getUID() + "." + type);
	}

	protected static boolean exists(HumanNPC npc) {
		return profiles.pathExists(npc.getUID());
	}

	public static BasicProperties getBasic() {
		return (BasicProperties) get("basic");
	}

	public static void load(HumanNPC npc) {
		for (Saveable saveable : properties.values()) {
			if (exists(npc) && saveable.getEnabled(npc)) {
				saveable.loadState(npc);
			}
		}
	}

	public static void load(String type, HumanNPC npc) {
		if (exists(npc) && get(type).getEnabled(npc))
			get(type).loadState(npc);
	}

	public static void save(HumanNPC npc) {
		for (Saveable saveable : properties.values()) {
			if (saveable.getEnabled(npc)) {
				saveable.saveState(npc);
			}
		}
	}

	public static void save(String type, HumanNPC npc) {
		if (exists(npc) && get(type).getEnabled(npc))
			get(type).saveState(npc);
	}

	public static void remove(HumanNPC npc) {
		profiles.removeKey(npc.getUID());
	}

	public static Saveable get(String string) {
		return properties.get(string);
	}

	public static void copyNPCs(int UID, int newUID) {
		for (Saveable saveable : properties.values()) {
			saveable.copy(UID, newUID);
		}
	}

	private static void saveAllNPCs() {
		for (HumanNPC npc : NPCManager.getList().values()) {
			save(npc);
		}
	}

	public static void stateSave() {
		saveAllNPCs();
	}
}