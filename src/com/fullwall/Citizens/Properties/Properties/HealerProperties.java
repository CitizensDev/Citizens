package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerProperties extends Saveable {
	private final PropertyHandler healers = new PropertyHandler(
			"plugins/Citizens/Healers/Citizens.healers");
	private final PropertyHandler strength = new PropertyHandler(
			"plugins/Citizens/Healers/Citizens.strength");
	private final PropertyHandler levels = new PropertyHandler(
			"plugins/Citizens/Healers/Citizens.levels");

	private void saveHealth(int UID, int healPower) {
		strength.setInt(UID, healPower);
	}

	private int getHealth(int UID) {
		return strength.getInt(UID);
	}

	private void saveLevel(int UID, int level) {
		levels.setInt(UID, level);
	}

	private int getLevel(int UID) {
		return levels.getInt(UID);
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
	public void saveFiles() {
		healers.save();
		strength.save();
		levels.save();
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setHealer(getEnabled(npc));
		npc.getHealer().setHealth(getHealth(npc.getUID()));
		npc.getHealer().setLevel(getLevel(npc.getUID()));
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		healers.removeKey(npc.getUID());
		strength.removeKey(npc.getUID());
		levels.removeKey(npc.getUID());
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		healers.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return healers.getBoolean(npc.getUID());
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return healers.keyExists(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.HEALER;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (healers.keyExists(UID)) {
			healers.setString(nextUID, healers.getString(UID));
		}
		if (strength.keyExists(UID)) {
			strength.setString(nextUID, strength.getString(UID));
		}
		if (levels.keyExists(UID)) {
			levels.setString(nextUID, levels.getString(UID));
		}
	}
}