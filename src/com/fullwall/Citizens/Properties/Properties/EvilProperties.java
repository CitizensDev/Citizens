package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyHandler;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class EvilProperties extends Saveable {
	private PropertyHandler evilNPCs = new PropertyHandler(
			"plugins/Citizens/Evil NPCs/evilNPCs.citizens");

	@Override
	public void saveFiles() {
		evilNPCs.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isEvil());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setEvil(getEnabled(npc));
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		evilNPCs.removeKey(npc.getUID());
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		evilNPCs.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return evilNPCs.getBoolean(npc.getUID());
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return evilNPCs.keyExists(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.EVIL;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (evilNPCs.keyExists(UID)) {
			evilNPCs.setString(nextUID, evilNPCs.getString(UID));
		}
	}
}