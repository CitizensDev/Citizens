package com.citizens;

import com.citizens.Properties.PropertyHandler;
import com.citizens.Properties.PropertyManager;

// TODO REMOVE AFTER 1.0.9 IS RELEASED
public class Conversion extends PropertyManager {

	private static final String dir = "plugins/Citizens/";
	private static final PropertyHandler inventories = new PropertyHandler(dir
			+ "Basic NPCs/inventories.citizens", false);
	private static final PropertyHandler texts = new PropertyHandler(dir
			+ "Basic NPCs/Citizens.texts", false);
	private static final PropertyHandler colors = new PropertyHandler(dir
			+ "Basic NPCs/Citizens.colours", false);
	private static final PropertyHandler items = new PropertyHandler(dir
			+ "Basic NPCs/Citizens.items", false);
	private static final PropertyHandler locations = new PropertyHandler(dir
			+ "Basic NPCs/Citizens.locations", false);
	private static final PropertyHandler lookat = new PropertyHandler(dir
			+ "Basic NPCs/Citizens.lookat", false);
	private static final PropertyHandler owners = new PropertyHandler(dir
			+ "Basic NPCs/Citizens.owners", false);
	private static final PropertyHandler talkWhenClose = new PropertyHandler(
			dir + "Basic NPCs/Citizens.talkWhenClose", false);
	private static final PropertyHandler blacksmiths = new PropertyHandler(dir
			+ "Blacksmiths/Citizens.blacksmiths", false);
	private static final PropertyHandler healers = new PropertyHandler(dir
			+ "Healers/Citizens.healers", false);
	private static final PropertyHandler strength = new PropertyHandler(dir
			+ "Healers/Citizens.strength", false);
	private static final PropertyHandler level = new PropertyHandler(dir
			+ "Healers/Citizens.levels", false);
	private static final PropertyHandler balances = new PropertyHandler(dir
			+ "Traders/Citizens.balances", false);
	private static final PropertyHandler stocking = new PropertyHandler(dir
			+ "Traders/Citizens.stocking", false);
	private static final PropertyHandler traders = new PropertyHandler(dir
			+ "Traders/Citizens.traders", false);
	private static final PropertyHandler unlimited = new PropertyHandler(dir
			+ "Traders/Citizens.unlimited", false);
	private static final PropertyHandler wizLocs = new PropertyHandler(dir
			+ "Wizards/Citizens.balances", false);
	private static final PropertyHandler wizards = new PropertyHandler(dir
			+ "Wizards/Citizens.wizards", false);
	private static final PropertyHandler mana = new PropertyHandler(dir
			+ "Wizards/mana.citizens", false);
	private static final PropertyHandler mode = new PropertyHandler(dir
			+ "Wizards/mode.citizens", false);

	public static void convert(int UID, String name) {
		profiles.setString(UID + ".basic.name", name);
		if (inventories.keyExists(UID)) {
			profiles.setString(UID + ".basic.inventory",
					inventories.getString(UID));
		}
		if (texts.keyExists(UID)) {
			profiles.setString(UID + ".basic.text", texts.getString(UID));
		}
		if (colors.keyExists(UID)) {
			profiles.setInt(UID + ".basic.color", colors.getInt(UID));
		}
		if (items.keyExists(UID)) {
			profiles.setString(UID + ".basic.items", items.getString(UID));
		}
		if (locations.keyExists(UID)) {
			profiles.setString(UID + ".basic.location",
					locations.getString(UID));
		}
		if (lookat.keyExists(UID)) {
			profiles.setBoolean(UID + ".basic.look-when-close",
					lookat.getBoolean(UID));
		}
		if (owners.keyExists(UID)) {
			profiles.setString(UID + ".basic.owner", owners.getString(UID));
		}
		if (talkWhenClose.keyExists(UID)) {
			profiles.setBoolean(UID + ".basic.talk-when-close",
					talkWhenClose.getBoolean(UID));
		}
		if (blacksmiths.keyExists(UID)) {
			profiles.setBoolean(UID + ".blacksmith.toggle",
					blacksmiths.getBoolean(UID));
		}
		if (healers.keyExists(UID)) {
			profiles.setBoolean(UID + ".healer.toggle", healers.getBoolean(UID));
		}
		if (strength.keyExists(UID)) {
			profiles.setInt(UID + ".healer.health", strength.getInt(UID));
		}
		if (level.keyExists(UID)) {
			profiles.setInt(UID + ".healer.level", level.getInt(UID));
		}
		if (balances.keyExists(UID)) {
			profiles.setDouble(UID + ".trader.balance", balances.getDouble(UID));
		}
		if (stocking.keyExists(UID)) {
			profiles.setString(UID + ".trader.stock", stocking.getString(UID));
		}
		if (traders.keyExists(UID)) {
			profiles.setBoolean(UID + ".trader.toggle", traders.getBoolean(UID));
		}
		if (unlimited.keyExists(UID)) {
			profiles.setBoolean(UID + ".trader.unlimited",
					unlimited.getBoolean(UID));
		}
		if (wizLocs.keyExists(UID)) {
			profiles.setString(UID + ".wizard.locations",
					wizLocs.getString(UID));
		}
		if (wizards.keyExists(UID)) {
			profiles.setBoolean(UID + ".wizard.toggle", wizards.getBoolean(UID));
		}
		if (mana.keyExists(UID)) {
			profiles.setInt(UID + ".wizard.mana", mana.getInt(UID));
		}
		if (mode.keyExists(UID)) {
			profiles.setString(UID + ".wizard.mode", mode.getString(UID));
		}
		profiles.save();
	}
}