package net.citizensnpcs.questers.data;

import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.properties.DataSource;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.quests.Quest;
import net.citizensnpcs.questers.quests.QuestParser;

public class QuestProperties {
	private static DataSource quests = new ConfigurationHandler(
			"plugins/Citizens/quests.yml");

	public static void load() {
		quests.load();
		QuestParser.parse(quests);
	}

	public static void save() {
		quests.save();
		for (Quest quest : QuestManager.quests()) {
			QuestParser.saveQuest(quests.getKey(quest.getName()), quest);
		}
	}

	public static void saveQuest(Quest quest) {
		QuestParser.saveQuest(quests.getKey(quest.getName()), quest);
	}
}