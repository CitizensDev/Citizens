package com.fullwall.Citizens.Properties.Properties;

import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestFactory;
import com.fullwall.Citizens.Properties.ConfigurationHandler;

public class QuestProperties {
	private static ConfigurationHandler quests;

	public static void initialize() {
		quests = new ConfigurationHandler("plugins/Citizens/economy.yml", false);
		QuestFactory.instantiateQuests(quests);
	}

	public static void save() {
		quests.save();
	}
}
