package com.citizens.Properties.Properties;

import com.citizens.NPCTypes.Questers.Quest;
import com.citizens.NPCTypes.Questers.Quests.QuestFactory;
import com.citizens.Properties.ConfigurationHandler;

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
