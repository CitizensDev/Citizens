package com.fullwall.Citizens.Properties.Properties;

import org.bukkit.entity.CreatureType;

import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.NPCTypes.Wizards.WizardManager.WizardMode;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardProperties extends PropertyManager implements Saveable {
	private static final String isWizard = ".wizard.toggle";
	private static final String locations = ".wizard.locations";
	private static final String mana = ".wizard.mana";
	private static final String mode = ".wizard.mode";
	private static final String time = ".wizard.time";
	private static final String mob = ".wizard.mob";

	public static void setEnabled(int UID, boolean enabled) {
		profiles.setBoolean(UID + isWizard, enabled);
	}

	public static boolean getEnabled(int UID) {
		return profiles.getBoolean(UID + isWizard);
	}

	public static void saveLocations(int UID, String locationString) {
		profiles.setString(UID + locations, locationString.replace(")(", "):("));
	}

	public static String getLocations(int UID) {
		return profiles.getString(UID + locations).replace(")(", "):(");
	}

	public static void saveMana(int UID, int manaLevel) {
		profiles.setInt(UID + mana, manaLevel);
	}

	public static int getMana(int UID) {
		return profiles.getInt(UID + mana, 10);
	}

	public static void saveMode(int UID, WizardMode wizardMode) {
		profiles.setString(UID + mode, wizardMode.toString());
	}

	public static WizardMode getMode(int UID) {
		if (profiles.pathExists(UID + mode)) {
			return WizardMode.parse(profiles.getString(UID + mode));
		}
		return WizardMode.TELEPORT;
	}

	public static void saveTime(int UID, String worldTime) {
		profiles.setString(UID + time, worldTime);
	}

	public static String getTime(int UID) {
		return profiles.getString(UID + time, "morning");
	}

	public static void saveMob(int UID, CreatureType type) {
		profiles.setString(UID + mob, type.getName());
	}

	public static CreatureType getMob(int UID) {
		if (CreatureType.fromName(profiles.getString(UID + mob)) != null) {
			return CreatureType.fromName(profiles.getString(UID + mob));
		}
		return CreatureType.CREEPER;
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc.getUID(), npc.isWizard());
			saveLocations(npc.getUID(), npc.getWizard().getLocations());
			saveMana(npc.getUID(), npc.getWizard().getMana());
			saveMode(npc.getUID(), npc.getWizard().getMode());
			saveTime(npc.getUID(), npc.getWizard().getTime());
			saveMob(npc.getUID(), npc.getWizard().getMob());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setWizard(getEnabled(npc));
		npc.getWizard().setLocations(getLocations(npc.getUID()));
		npc.getWizard().setMana(getMana(npc.getUID()));
		npc.getWizard().setMode(getMode(npc.getUID()));
		npc.getWizard().setTime(getTime(npc.getUID()));
		npc.getWizard().setMob(getMob(npc.getUID()));
		saveState(npc);
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isWizard);
	}

	@Override
	public PropertyType type() {
		return PropertyType.WIZARD;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + isWizard)) {
			profiles.setString(nextUID + isWizard,
					profiles.getString(UID + isWizard));
		}
		if (profiles.pathExists(UID + locations)) {
			profiles.setString(nextUID + locations,
					profiles.getString(UID + locations));
		}
		if (profiles.pathExists(UID + mana)) {
			profiles.setString(nextUID + mana, profiles.getString(UID + mana));
		}
		if (profiles.pathExists(UID + mode)) {
			profiles.setString(nextUID + mode, profiles.getString(UID + mode));
		}
		if (profiles.pathExists(UID + time)) {
			profiles.setString(nextUID + time, profiles.getString(UID + time));
		}
		if (profiles.pathExists(UID + mob)) {
			profiles.setString(nextUID + mob, profiles.getString(UID + mob));
		}
	}
}