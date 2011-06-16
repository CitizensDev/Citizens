package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithProperties extends PropertyManager implements Saveable {
	private final String isBlacksmith = ".blacksmith.toggle";

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