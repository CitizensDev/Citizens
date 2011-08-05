package net.citizensnpcs.questers;

import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.questers.quests.QuestFactory;

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