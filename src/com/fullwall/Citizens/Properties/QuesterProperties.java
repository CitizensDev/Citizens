package com.fullwall.Citizens.Properties;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuesterProperties extends Saveable {
	public PropertyHandler questers;

	public QuesterProperties() {
		questers = new PropertyHandler(
				"plugins/Citizens/Questers/Citizens.questers");
	}

	public void saveQuester(int UID, boolean state) {
		questers.setBoolean(UID, state);
	}

	public boolean isQuester(int UID) {
		return questers.keyExists(UID);
	}

	public boolean getQuesterState(int UID) {
		return questers.getBoolean(UID);
	}

	public void removeQuester(int UID) {
		questers.removeKey(UID);
	}

	/**
	 * Copies all data from one ID to another.
	 * 
	 * @param UID
	 * @param nextUID
	 */
	@Override
	public void copy(int UID, int nextUID) {
		if (questers.keyExists(UID))
			questers.setString(nextUID, questers.getString(UID));
		saveFiles();
	}

	@Override
	public void saveFiles() {
		questers.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			set(npc, npc.isQuester());
		}
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		questers.removeKey(npc.getUID());
	}

	@Override
	public void set(HumanNPC npc) {
		set(npc, true);
	}

	@Override
	public void set(HumanNPC npc, boolean value) {
		saveQuester(npc.getUID(), value);
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return isQuester(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.QUESTER;
	}
}