package com.fullwall.Citizens.Properties;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.Properties.BasicProperties;
import com.fullwall.Citizens.Properties.Properties.BlacksmithProperties;
import com.fullwall.Citizens.Properties.Properties.HealerProperties;
import com.fullwall.Citizens.Properties.Properties.QuesterProperties;
import com.fullwall.Citizens.Properties.Properties.TraderProperties;
import com.fullwall.Citizens.Properties.Properties.WizardProperties;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class PropertyManager {
	public static HashMap<String, Saveable> properties = new HashMap<String, Saveable>();
	private static BasicProperties basicProperties = new BasicProperties();

	public enum PropertyType {
		BASIC, TRADER, HEALER, WIZARD, QUESTER, BLACKSMITH;
	}

	public static void registerProperties() {
		properties.put("basic", new BasicProperties());
		properties.put("blacksmith", new BlacksmithProperties());
		properties.put("healer", new HealerProperties());
		properties.put("quester", new QuesterProperties());
		properties.put("trader", new TraderProperties());
		properties.put("wizard", new WizardProperties());

		setBasicProperties();
	}

	private static void setBasicProperties() {
		basicProperties = (BasicProperties) properties.get("basic");
	}

	public static BasicProperties getBasicProperties() {
		return basicProperties;
	}

	public static void load(HumanNPC npc) {
		for (Saveable saveable : properties.values()) {
			saveable.loadState(npc);
		}
	}

	public static void save(HumanNPC npc) {
		for (Saveable saveable : properties.values()) {
			if (saveable.exists(npc))
				saveable.saveState(npc);
		}
	}

	public static void saveFiles() {
		for (Saveable saveable : properties.values()) {
			saveable.saveFiles();
		}
	}

	public static PropertyHandler getHandler(Class<?> passedClass,
			String fieldName, Saveable saveable) {
		try {
			Field f = passedClass.getDeclaredField(fieldName);
			PropertyHandler handler = (PropertyHandler) f.get(saveable);
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
}
