package com.fullwall.Citizens.Questers;

import com.fullwall.Citizens.PropertyHandler;

public class QuestPropertyPool {
	public static final PropertyHandler quest = new PropertyHandler(
			"plugins/Citizens/Questers/Quests/");
	
	public static String getQuestName() {
		return quest.getString("quest-name");
	}

	public static void saveQuestName(String name) {
		quest.setString(getQuestName(), name);
	}

	public static String getQuestType() {
		return quest.getString("quest-type");
	}

	public static void saveQuestType(String type) {
		quest.setString(getQuestType(), type);
	}

	public static String getStartNPC() {
		return quest.getString("start-npc");
	}

	public static void saveStartNPC(String name) {
		quest.setString(getStartNPC(), name);
	}

	public static String getDescription() {
		return quest.getString("description");
	}

	public static void saveDescription(String desc) {
		quest.setString(getDescription(), desc);
	}

	public static String getCompletionText() {
		return quest.getString("completion-text");
	}

	public static void saveCompletionText(String text) {
		quest.setString(getCompletionText(), text);
	}
}
