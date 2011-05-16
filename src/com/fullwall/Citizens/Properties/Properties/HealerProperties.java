package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerProperties extends Saveable {
	public PropertyHandler healers = new PropertyHandler(
			"plugins/Citizens/Healers/Citizens.healers");
	public PropertyHandler strength = new PropertyHandler(
			"plugins/Citizens/Healers/Citizens.strength");
	public PropertyHandler levels = new PropertyHandler(
			"plugins/Citizens/Healers/Citizens.levels");

	public void removeHealer(int UID) {
		healers.removeKey(UID);
	}

	public void saveStrength(int UID, int healPower) {
		strength.setInt(UID, healPower);
	}

	public int getStrength(int UID) {
		return strength.getInt(UID);
	}

	public void saveLevel(int UID, int level) {
		levels.setInt(UID, level);
	}

	public int getLevel(int UID) {
		return levels.getInt(UID);
	}

	public boolean getHealerState(int UID) {
		return healers.getBoolean(UID);
	}

	public int getMaxStrength(int UID) {
		return getLevel(UID) * 10;
	}

	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isHealer());
			saveStrength(npc.getUID(), npc.getHealer().getStrength());
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
		npc.getHealer().setStrength(getStrength(npc.getUID()));
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