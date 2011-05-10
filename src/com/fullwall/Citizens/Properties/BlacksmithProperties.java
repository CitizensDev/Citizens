package com.fullwall.Citizens.Properties;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithProperties extends Saveable {
	public PropertyHandler blacksmiths;

	public BlacksmithProperties() {
		blacksmiths = new PropertyHandler(
				"plugins/Citizens/Blacksmiths/Citizens.blacksmiths");
	}

	public void setBlacksmith(int UID, boolean state) {
		blacksmiths.setBoolean(UID, state);
	}

	public boolean isBlacksmith(int UID) {
		return blacksmiths.keyExists(UID);
	}

	public boolean getBlacksmithState(int UID) {
		return blacksmiths.getBoolean(UID);
	}

	/**
	 * Copies all data from one ID to another.
	 * 
	 * @param UID
	 * @param nextUID
	 */
	@Override
	public void copy(int UID, int nextUID) {
		if (blacksmiths.keyExists(UID))
			blacksmiths.setString(nextUID, blacksmiths.getString(UID));
		saveFiles();
	}

	@Override
	public void saveFiles() {
		blacksmiths.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			set(npc, npc.isBlacksmith());
		}
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		blacksmiths.removeKey(npc.getUID());
	}

	@Override
	public void set(HumanNPC npc) {
		set(npc, true);
	}

	@Override
	public void set(HumanNPC npc, boolean value) {
		setBlacksmith(npc.getUID(), value);
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return isBlacksmith(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.BLACKSMITH;
	}
}