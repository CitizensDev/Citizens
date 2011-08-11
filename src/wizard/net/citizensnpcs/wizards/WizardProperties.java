package net.citizensnpcs.wizards;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.SettingsManager.SettingsType;
import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.wizards.WizardManager.WizardMode;

import org.bukkit.entity.CreatureType;

public class WizardProperties extends PropertyManager implements Properties {
	private static final String isWizard = ".wizard.toggle";
	private static final String locations = ".wizard.locations";
	private static final String mana = ".wizard.mana";
	private static final String mode = ".wizard.mode";
	private static final String time = ".wizard.time";
	private static final String mob = ".wizard.mob";
	private static final String unlimitedMana = ".wizard.unlimited-mana";
	private static final String command = ".wizard.command";

	private void saveLocations(int UID, String locationString) {
		profiles.setString(UID + locations, locationString.replace(")(", "):("));
	}

	private String getLocations(int UID) {
		return profiles.getString(UID + locations).replace(")(", "):(");
	}

	private void saveMana(int UID, int manaLevel) {
		profiles.setInt(UID + mana, manaLevel);
	}

	private int getMana(int UID) {
		return profiles.getInt(UID + mana, 10);
	}

	private void saveMode(int UID, WizardMode wizardMode) {
		profiles.setString(UID + mode, wizardMode.toString());
	}

	private WizardMode getMode(int UID) {
		if (profiles.pathExists(UID + mode)) {
			return WizardMode.parse(profiles.getString(UID + mode));
		}
		return WizardMode.TELEPORT;
	}

	private void saveTime(int UID, String worldTime) {
		profiles.setString(UID + time, worldTime);
	}

	private String getTime(int UID) {
		return profiles.getString(UID + time, "morning");
	}

	private void saveMob(int UID, CreatureType type) {
		profiles.setString(UID + mob, type.getName());
	}

	private CreatureType getMob(int UID) {
		if (CreatureType.fromName(profiles.getString(UID + mob)) != null) {
			return CreatureType.fromName(profiles.getString(UID + mob));
		}
		return CreatureType.CREEPER;
	}

	private void saveUnlimitedMana(int UID, boolean unlimited) {
		profiles.setBoolean(UID + unlimitedMana, unlimited);
	}

	private boolean hasUnlimitedMana(int UID) {
		return profiles.getBoolean(UID + unlimitedMana);
	}

	private void saveCommand(int UID, String cmd) {
		profiles.setString(UID + command, cmd);
	}

	private String getCommand(int UID) {
		return profiles.getString(UID + command);
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			boolean is = npc.isType("wizard");
			setEnabled(npc, is);
			if (is) {
				Wizard wizard = npc.getType("wizard");
				saveLocations(npc.getUID(), wizard.getLocations());
				saveMana(npc.getUID(), wizard.getMana());
				saveMode(npc.getUID(), wizard.getMode());
				saveTime(npc.getUID(), wizard.getTime());
				saveMob(npc.getUID(), wizard.getMob());
				saveUnlimitedMana(npc.getUID(), wizard.hasUnlimitedMana());
				saveCommand(npc.getUID(), wizard.getCommand());
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("wizard");
			Wizard wizard = npc.getType("wizard");
			wizard.setLocations(getLocations(npc.getUID()));
			wizard.setMana(getMana(npc.getUID()));
			wizard.setMode(getMode(npc.getUID()));
			wizard.setTime(getTime(npc.getUID()));
			wizard.setMob(getMob(npc.getUID()));
			wizard.setUnlimitedMana(hasUnlimitedMana(npc.getUID()));
			wizard.setCommand(getCommand(npc.getUID()));
		}
		saveState(npc);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isWizard, value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isWizard);
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
		if (profiles.pathExists(UID + unlimitedMana)) {
			profiles.setString(nextUID + unlimitedMana,
					profiles.getString(UID + unlimitedMana));
		}
		if (profiles.pathExists(UID + command)) {
			profiles.setString(nextUID + command,
					profiles.getString(UID + command));
		}
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("", SettingsType.ECONOMY, "prices.wizard.teleport",
				100));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.wizard.changetime", 100));
		nodes.add(new Node("", SettingsType.ECONOMY, "prices.wizard.spawnmob",
				100));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.wizard.togglestorm", 100));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.wizard.executecommand", 100));
		nodes.add(new Node("WizardMaxLocations", SettingsType.GENERAL,
				"general.wizards.wizard-max-locations", 10));
		nodes.add(new Node("WizardMaxMana", SettingsType.GENERAL,
				"general.wizards.max-mana", 100));
		nodes.add(new Node("WizardInteractItem", SettingsType.GENERAL,
				"items.wizards.interact-item", 288));
		nodes.add(new Node("WizardManaRegenItem", SettingsType.GENERAL,
				"items.wizards.mana-regen-item", 348));
		nodes.add(new Node("WizardManaRegenRate", SettingsType.GENERAL,
				"ticks.wizards.mana-regen-rate", 6000));
		nodes.add(new Node("RegenWizardMana", SettingsType.GENERAL,
				"general.wizards.regen-mana", true));
		return nodes;
	}
}