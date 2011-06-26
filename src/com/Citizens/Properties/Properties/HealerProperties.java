package com.citizens.Properties.Properties;

import com.citizens.Interfaces.Saveable;
import com.citizens.NPCTypes.Healers.HealerNPC;
import com.citizens.NPCs.NPCManager;
import com.citizens.Properties.PropertyManager;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;

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
			boolean is = npc.isType("healer");
			setEnabled(npc, is);
			if (is) {
				HealerNPC healer = npc.getToggleable("healer");
				saveHealth(npc.getUID(), healer.getHealth());
				saveLevel(npc.getUID(), healer.getLevel());
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("healer", NPCManager.getFactory("healer"));
			HealerNPC healer = npc.getToggleable("healer");
			healer.setHealth(getHealth(npc.getUID()));
			healer.setLevel(getLevel(npc.getUID()));
		}
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