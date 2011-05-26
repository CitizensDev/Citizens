package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyHandler;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithProperties extends Saveable {
	private final PropertyHandler blacksmiths = new PropertyHandler(
			"plugins/Citizens/Blacksmiths/Citizens.blacksmiths");

	@Override
	public void saveFiles() {
		blacksmiths.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isBlacksmith());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setBlacksmith(getEnabled(npc));
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		blacksmiths.removeKey(npc.getUID());
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		blacksmiths.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return blacksmiths.getBoolean(npc.getUID());
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return blacksmiths.keyExists(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.BLACKSMITH;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (blacksmiths.keyExists(UID)) {
			blacksmiths.setString(nextUID, blacksmiths.getString(UID));
		}
	}
}