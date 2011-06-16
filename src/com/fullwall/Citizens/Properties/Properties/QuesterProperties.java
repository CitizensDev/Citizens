package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuesterProperties extends PropertyManager implements Saveable {
	private static final String isQuester = ".quester.toggle";
	private static final String quests = ".quester.quests";

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isQuester());
			setQuests(npc);
		}
	}

	private void setQuests(HumanNPC npc) {
		String write = "";
		for (String quest : npc.getQuester().getQuests()) {
			write = write + quest + ";";
		}
		profiles.setString(npc.getUID() + quests, write);
	}

	private String getQuests(HumanNPC npc) {
		if (profiles.pathExists(npc.getUID() + quests)) {
			for (String quest : profiles.getString(npc.getUID() + quests)
					.split(";")) {
				npc.getQuester().addQuest(quest);
			}
			return profiles.getString(npc.getUID() + quests);
		}
		return "";
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setQuester(getEnabled(npc));
		getQuests(npc);
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
	public PropertyType type() {
		return PropertyType.QUESTER;
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