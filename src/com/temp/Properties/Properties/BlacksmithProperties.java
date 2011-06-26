package com.temp.Properties.Properties;

import com.temp.Interfaces.Saveable;
import com.temp.NPCs.NPCManager;
import com.temp.Properties.PropertyManager;
import com.temp.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithProperties extends PropertyManager implements Saveable {
	private final String isBlacksmith = ".blacksmith.toggle";

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isType("blacksmith"));
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc))
			npc.registerType("blacksmith", NPCManager.getFactory("blacksmith"));
		saveState(npc);
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isBlacksmith, value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isBlacksmith);
	}

	@Override
	public PropertyType type() {
		return PropertyType.BLACKSMITH;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + isBlacksmith)) {
			profiles.setString(nextUID + isBlacksmith,
					profiles.getString(UID + isBlacksmith));
		}
	}
}