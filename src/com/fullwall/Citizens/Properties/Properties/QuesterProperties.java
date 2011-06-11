package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyHandler;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuesterProperties extends Saveable {
	private final PropertyHandler questers = new PropertyHandler(
			"plugins/Citizens/Questers/questers.citizens");

	@Override
	public void saveFiles() {
		questers.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isQuester());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setQuester(getEnabled(npc));
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		questers.removeKey(npc.getUID());
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		questers.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return questers.getBoolean(npc.getUID());
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return questers.keyExists(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.QUESTER;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (questers.keyExists(UID)) {
			questers.setString(nextUID, questers.getString(UID));
		}
	}
}