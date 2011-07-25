package com.citizens.properties.properties;

import com.citizens.interfaces.Saveable;
import com.citizens.npctypes.questers.Quester;
import com.citizens.properties.PropertyManager;
import com.citizens.resources.npclib.HumanNPC;

public class QuesterProperties extends PropertyManager implements Saveable {
	private static final String isQuester = ".quester.toggle";
	private static final String quests = ".quester.quests";

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isType("quester"));
			setQuests(npc);
		}
	}

	private void setQuests(HumanNPC npc) {
		String write = "";
		Quester quester = npc.getType("quester");
		for (String quest : quester.getQuests()) {
			write = write + quest + ";";
		}
		profiles.setString(npc.getUID() + quests, write);
	}

	private String getQuests(HumanNPC npc) {
		if (profiles.pathExists(npc.getUID() + quests)) {
			Quester quester = npc.getType("quester");
			for (String quest : profiles.getString(npc.getUID() + quests)
					.split(";")) {
				quester.addQuest(quest);
			}
			return profiles.getString(npc.getUID() + quests);
		}
		return "";
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("quester");
			getQuests(npc);
		}
		saveState(npc);
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isQuester, value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isQuester);
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + isQuester)) {
			profiles.setString(nextUID + isQuester,
					profiles.getString(UID + isQuester));
		}
		if (profiles.pathExists(UID + quests)) {
			profiles.setString(nextUID + quests,
					profiles.getString(UID + quests));
		}
	}
}