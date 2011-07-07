package com.citizens.properties.properties;

import com.citizens.npctypes.questers.Quest;
import com.citizens.npctypes.questers.quests.QuestFactory;
import com.citizens.properties.ConfigurationHandler;

public class QuestProperties {
	private static ConfigurationHandler quests;

	public static void initialize() {
		quests = new ConfigurationHandler("plugins/Citizens/quests.yml");
		QuestFactory.instantiateQuests(quests);
	}

	public static void save() {
		quests.save();
	}

	public static void saveQuest(Quest quest) {
		QuestFactory.saveQuest(quests, quest);
	}
}
