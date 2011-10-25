package net.citizensnpcs.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.citizensnpcs.SettingsManager.SettingsType;
import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

import com.google.common.collect.Lists;

public class WizardProperties extends PropertyManager implements Properties {
	public static final WizardProperties INSTANCE = new WizardProperties();

	private WizardProperties() {
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			boolean is = npc.isType("wizard");
			setEnabled(npc, is);
			if (is) {
				Wizard wizard = npc.getType("wizard");
				wizard.save(profiles, npc.getUID());
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("wizard");
			Wizard wizard = npc.getType("wizard");
			wizard.load(profiles, npc.getUID());
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
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.wizard.teleport", 100));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.wizard.changetime", 100));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.wizard.spawnmob", 100));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.wizard.togglestorm", 100));
		nodes.add(new Node("TeleportManaCost", SettingsType.GENERAL,
				"wizards.mana-costs.teleport", 5));
		nodes.add(new Node("ChangeTimeManaCost", SettingsType.GENERAL,
				"wizards.mana-costs.changetime", 5));
		nodes.add(new Node("SpawnMobManaCost", SettingsType.GENERAL,
				"wizards.mana-costs.spawnmob", 5));
		nodes.add(new Node("ToggleStormManaCost", SettingsType.GENERAL,
				"wizards.mana-costs.togglestorm", 5));
		nodes.add(new Node("WizardMaxLocations", SettingsType.GENERAL,
				"wizards.max-locations", 10));
		nodes.add(new Node("WizardMaxMana", SettingsType.GENERAL,
				"wizards.max-mana", 100));
		nodes.add(new Node("WizardInteractItem", SettingsType.GENERAL,
				"wizards.interact-item", 288));
		nodes.add(new Node("WizardManaRegenItem", SettingsType.GENERAL,
				"wizards.mana-regen-item", 348));
		nodes.add(new Node("WizardManaRegenRate", SettingsType.GENERAL,
				"wizards.mana-regen-rate", 6000));
		nodes.add(new Node("RegenWizardMana", SettingsType.GENERAL,
				"wizards.regen-mana", true));
		return nodes;
	}

	@Override
	public Collection<String> getNodesForCopy() {
		return nodesForCopy;
	}

	private static final List<String> nodesForCopy = Lists.newArrayList(
			"wizard.toggle", "wizard.locations", "wizard.mana", "wizard.mode",
			"wizard.time", "wizard.mob", "wizard.unlimited-mana");

	private static final String isWizard = ".wizard.toggle";
}