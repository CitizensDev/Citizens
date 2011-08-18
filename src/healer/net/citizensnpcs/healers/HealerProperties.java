package net.citizensnpcs.healers;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.SettingsManager.SettingsType;
import net.citizensnpcs.api.Node;
import net.citizensnpcs.api.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

public class HealerProperties extends PropertyManager implements Properties {
	private final String isHealer = ".healer.toggle";
	private final String health = ".healer.health";
	private final String level = ".healer.level";

	public static final HealerProperties INSTANCE = new HealerProperties();

	private HealerProperties() {
	}

	private void saveHealth(int UID, int healPower) {
		profiles.setInt(UID + health, healPower);
	}

	private int getHealth(int UID) {
		return profiles.getInt(UID + health, 10);
	}

	private void saveLevel(int UID, int currentLevel) {
		profiles.setInt(UID + level, currentLevel);
	}

	private int getLevel(int UID) {
		return profiles.getInt(UID + level, 1);
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			boolean is = npc.isType("healer");
			setEnabled(npc, is);
			if (is) {
				Healer healer = npc.getType("healer");
				saveHealth(npc.getUID(), healer.getHealth());
				saveLevel(npc.getUID(), healer.getLevel());
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("healer");
			Healer healer = npc.getType("healer");
			healer.setHealth(getHealth(npc.getUID()));
			healer.setLevel(getLevel(npc.getUID()));
		}
		saveState(npc);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isHealer, value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isHealer);
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + isHealer)) {
			profiles.setString(nextUID + isHealer,
					profiles.getString(UID + isHealer));
		}
		if (profiles.pathExists(UID + health)) {
			profiles.setString(nextUID + health,
					profiles.getString(UID + health));
		}
		if (profiles.pathExists(UID + level)) {
			profiles.setString(nextUID + level, profiles.getString(UID + level));
		}
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.healer.levelup", 100));
		nodes.add(new Node("", SettingsType.GENERAL,
				"economy.prices.healer.heal", 100));
		nodes.add(new Node("HealerGiveHealthItem", SettingsType.GENERAL,
				"items.healers.give-health-item", 35));
		nodes.add(new Node("HealerTakeHealthItem", SettingsType.GENERAL,
				"items.healers.take-health-item", 276));
		nodes.add(new Node("HealerHealthRegenIncrement", SettingsType.GENERAL,
				"ticks.healers.health-regen-increment", 12000));
		nodes.add(new Node("RegenHealerHealth", SettingsType.GENERAL,
				"general.healers.regen-health", true));
		return nodes;
	}
}