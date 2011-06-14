package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyHandler;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuesterProperties extends Saveable {
	private final PropertyHandler questers = new PropertyHandler(
			"plugins/Citizens/Questers/questers.citizens");
	private final PropertyHandler quests = new PropertyHandler(
			"plugins/Citizens/Questers/quests.citizens");

	@Override
	public void saveFiles() {
		questers.save();
	}

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
		quests.setString(npc.getUID(), write);
	}

	private void loadQuests(HumanNPC npc) {
		for (String quest : quests.getString(npc.getUID()).split(";")) {
			npc.getQuester().addQuest(quest);
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setQuester(getEnabled(npc));
		loadQuests(npc);
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		questers.removeKey(npc.getUID());
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		questers.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return questers.getBoolean(npc.getUID());
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return questers.keyExists(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.QUESTER;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (questers.keyExists(UID)) {
			questers.setString(nextUID, questers.getString(UID));
		}
	}
}