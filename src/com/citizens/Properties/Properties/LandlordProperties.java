package com.citizens.Properties.Properties;

import com.citizens.Interfaces.Saveable;
import com.citizens.NPCs.NPCTypeManager;
import com.citizens.Properties.PropertyManager;
import com.citizens.Resources.NPClib.HumanNPC;

public class LandlordProperties extends PropertyManager implements Saveable {
	private final String isLandlord = ".landlord.toggle";

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isType("landlord"));
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("landlord",
					NPCTypeManager.getFactory("landlord"));
		}
		saveState(npc);
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isLandlord, value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isLandlord);
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + isLandlord)) {
			profiles.setString(nextUID + isLandlord,
					profiles.getString(UID + isLandlord));
		}
	}
}