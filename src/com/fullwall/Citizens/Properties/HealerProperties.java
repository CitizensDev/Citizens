package com.fullwall.Citizens.Properties;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerProperties extends Saveable {
	public PropertyHandler healers;
	public PropertyHandler strength;
	public PropertyHandler levels;

	public HealerProperties() {
		healers = new PropertyHandler(
				"plugins/Citizens/Healers/Citizens.healers");
		strength = new PropertyHandler(
				"plugins/Citizens/Healers/Citizens.strength");
		levels = new PropertyHandler("plugins/Citizens/Healers/Citizens.levels");
	}

	public void saveHealer(int UID, boolean state) {
		healers.setBoolean(UID, state);
	}

	public boolean isHealer(int UID) {
		return healers.keyExists(UID);
	}

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

	@Override
	public void saveFiles() {
		healers.save();
		strength.save();
		levels.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			set(npc, npc.isHealer());
			saveStrength(npc.getUID(), getStrength(npc.getUID()));
			saveLevel(npc.getUID(), getLevel(npc.getUID()));
		}
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		healers.removeKey(npc.getUID());
		strength.removeKey(npc.getUID());
		levels.removeKey(npc.getUID());
	}

	@Override
	public void set(HumanNPC npc) {
		set(npc, true);
	}

	@Override
	public void set(HumanNPC npc, boolean value) {
		saveHealer(npc.getUID(), value);
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return isHealer(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.HEALER;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (healers.keyExists(UID))
			healers.setString(nextUID, healers.getString(UID));
		if (strength.keyExists(UID))
			strength.setString(nextUID, strength.getString(UID));
		if (levels.keyExists(UID))
			levels.setString(nextUID, levels.getString(UID));
		saveFiles();
	}
}