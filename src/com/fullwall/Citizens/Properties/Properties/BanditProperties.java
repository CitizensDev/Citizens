package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BanditProperties extends Saveable {
	private final PropertyHandler bandits = new PropertyHandler(
			"plugins/Citizens/Bandits/Citizens.bandits");

	@Override
	public void saveFiles() {
		bandits.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isBandit());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setBandit(getEnabled(npc));
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		bandits.removeKey(npc.getUID());
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		bandits.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return bandits.getBoolean(npc.getUID());
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return bandits.keyExists(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.BANDIT;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (bandits.keyExists(UID)) {
			bandits.setString(nextUID, bandits.getString(UID));
		}
	}
}