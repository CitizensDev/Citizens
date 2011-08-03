package net.citizensnpcs.properties.properties;

import net.citizensnpcs.npctypes.questers.Quest;
import net.citizensnpcs.npctypes.questers.quests.QuestFactory;
import net.citizensnpcs.properties.ConfigurationHandler;

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