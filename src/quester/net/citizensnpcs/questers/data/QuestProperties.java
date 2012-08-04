package net.citizensnpcs.questers.data;

import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.quests.Quest;
import net.citizensnpcs.questers.quests.QuestFactory;

public class QuestProperties {
	private static ConfigurationHandler quests = new ConfigurationHandler(
			"plugins/Citizens/quests.yml");

	public static void initialize() {
		quests.load();
		QuestFactory.instantiateQuests(quests);
	}

	public static void load() {
		quests.load();
		QuestFactory.instantiateQuests(quests);
	}

	public static void save() {
		quests.save();
		for (Quest quest : QuestManager.quests()) {
			QuestFactory.saveQuest(quests, quest);
		}
	}

	public static void saveQuest(Quest quest) {
		QuestFactory.saveQuest(quests, quest);
	}
}