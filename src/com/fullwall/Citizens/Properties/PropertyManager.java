package com.fullwall.Citizens.Properties;

import java.util.HashMap;

import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.Properties.BasicProperties;
import com.fullwall.Citizens.Properties.Properties.BlacksmithProperties;
import com.fullwall.Citizens.Properties.Properties.GuardProperties;
import com.fullwall.Citizens.Properties.Properties.HealerProperties;
import com.fullwall.Citizens.Properties.Properties.QuestProperties;
import com.fullwall.Citizens.Properties.Properties.QuesterProperties;
import com.fullwall.Citizens.Properties.Properties.TraderProperties;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.Citizens.Properties.Properties.WizardProperties;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class PropertyManager {
	private static final HashMap<String, Saveable> properties = new HashMap<String, Saveable>();
	protected static final ConfigurationHandler profiles = new ConfigurationHandler(
			"plugins/Citizens/npc-profiles.yml");

	public enum PropertyType {
		BASIC, TRADER, HEALER, WIZARD, QUESTER, BLACKSMITH, GUARD;
	}

	public static void registerProperties() {
		properties.put("basic", new BasicProperties());
		properties.put("blacksmith", new BlacksmithProperties());
		properties.put("guard", new GuardProperties());
		properties.put("healer", new HealerProperties());
		properties.put("quester", new QuesterProperties());
		properties.put("trader", new TraderProperties());
		properties.put("wizard", new WizardProperties());
		UtilityProperties.initialize();
		QuestProperties.initialize();
	}

	public static ConfigurationHandler getProfiles() {
		return profiles;
	}

	public static boolean typeExists(HumanNPC npc, String type) {
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
			if (exists(npc)) {
				saveable.loadState(npc);
			}
		}
	}

	public static void save(HumanNPC npc) {
		for (Saveable saveable : properties.values()) {
			saveable.saveState(npc);
		}
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