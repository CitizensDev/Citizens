package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerProperties extends PropertyManager implements Saveable {
	private final String isHealer = ".healer.toggle";
	private final String health = ".healer.health";
	private final String level = ".healer.level";

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
			setEnabled(npc, npc.isHealer());
			saveHealth(npc.getUID(), npc.getHealer().getHealth());
			saveLevel(npc.getUID(), npc.getHealer().getLevel());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setHealer(getEnabled(npc));
		npc.getHealer().setHealth(getHealth(npc.getUID()));
		npc.getHealer().setLevel(getLevel(npc.getUID()));
		saveState(npc);
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
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
	public PropertyType type() {
		return PropertyType.HEALER;
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
}