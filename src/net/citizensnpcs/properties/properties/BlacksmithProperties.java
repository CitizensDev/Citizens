package net.citizensnpcs.properties.properties;

import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.properties.Saveable;
import net.citizensnpcs.resources.npclib.HumanNPC;

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
		if (getEnabled(npc)) {
			npc.registerType("blacksmith");
		}
		saveState(npc);
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
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + isBlacksmith)) {
			profiles.setString(nextUID + isBlacksmith,
					profiles.getString(UID + isBlacksmith));
		}
	}
}