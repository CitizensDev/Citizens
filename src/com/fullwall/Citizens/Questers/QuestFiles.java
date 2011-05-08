package com.fullwall.Citizens.Questers;

import com.fullwall.Citizens.PropertyHandler;

public class QuestFiles {
	public static PropertyHandler questFile;

	public static void createQuestFile(String questName) {
		questFile = new PropertyHandler("plugins/Citizens/Questers/Quests/" + questName + ".quests");
	}
}
