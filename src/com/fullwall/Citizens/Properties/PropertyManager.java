package com.fullwall.Citizens.Properties;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Interfaces.Storage;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.Properties.BanditProperties;
import com.fullwall.Citizens.Properties.Properties.BasicProperties;
import com.fullwall.Citizens.Properties.Properties.BlacksmithProperties;
import com.fullwall.Citizens.Properties.Properties.EvilProperties;
import com.fullwall.Citizens.Properties.Properties.GuardProperties;
import com.fullwall.Citizens.Properties.Properties.HealerProperties;
import com.fullwall.Citizens.Properties.Properties.QuesterProperties;
import com.fullwall.Citizens.Properties.Properties.TraderProperties;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.Citizens.Properties.Properties.WizardProperties;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class PropertyManager {
	public static HashMap<String, Saveable> properties = new HashMap<String, Saveable>();
	private static BasicProperties basicProperties;

	public enum PropertyType {
		BASIC, TRADER, HEALER, WIZARD, QUESTER, BLACKSMITH, BANDIT, GUARD, EVIL;
	}

	public static void registerProperties() {
		BasicProperties basic = new BasicProperties();
		properties.put("basic", basic);
		properties.put("bandit", new BanditProperties());
		properties.put("blacksmith", new BlacksmithProperties());
		properties.put("evil", new EvilProperties());
		properties.put("guard", new GuardProperties());
		properties.put("healer", new HealerProperties());
		properties.put("quester", new QuesterProperties());
		properties.put("trader", new TraderProperties());
		properties.put("wizard", new WizardProperties());
		basicProperties = basic;
		UtilityProperties.initialise();
	}

	public static BasicProperties getBasic() {
		return basicProperties;
	}

	public static void load(HumanNPC npc) {
		for (Saveable saveable : properties.values()) {
			if (saveable.exists(npc)) {
				saveable.loadState(npc);
			}
		}
	}

	public static void save(HumanNPC npc) {
		for (Saveable saveable : properties.values()) {
			saveable.saveState(npc);
		}
	}

	public static void saveFiles() {
		for (Saveable saveable : properties.values()) {
			saveable.saveFiles();
		}
	}

	public static Storage getHandler(Class<?> passedClass, String fieldName,
			Saveable saveable) {
		try {
			Field f = passedClass.getDeclaredField(fieldName);
			Storage handler = (Storage) f.get(saveable);
			return handler;
		} catch (Exception ex) {
			return null;
		}
	}

	public static void remove(HumanNPC npc) {
		for (Saveable saveable : properties.values()) {
			saveable.removeFromFiles(npc);
		}
	}

	public static Saveable get(String string) {
		return properties.get(string);
	}

	public static void copy(int UID, int newUID) {
		for (Saveable saveable : properties.values()) {
			saveable.copy(UID, newUID);
		}
	}

	public static void saveAllNPCs() {
		for (HumanNPC npc : NPCManager.getList().values()) {
			save(npc);
		}
	}

	public static void stateSave() {
		saveAllNPCs();
		saveFiles();
	}
}