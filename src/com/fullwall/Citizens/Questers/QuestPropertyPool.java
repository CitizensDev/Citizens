package com.fullwall.Citizens.Questers;

import com.fullwall.Citizens.PropertyHandler;

public class QuestPropertyPool {

	/**
	 * Save a quest file
	 * 
	 * @param quest
	 */
	public static void saveQuest(Quest quest) {
		PropertyHandler questData = new PropertyHandler(
				"plugins/Citizens/Questers/Quests/" + quest.getName()
						+ ".quest");
		questData.setString("name", quest.getName());
		questData.setString("type", quest.getType());
		questData.setString("start-npc", quest.getStartNPC().getStrippedName());
		questData.setInt("iConomy-reward", quest.getiConomyReward());
		questData.setInt("item-reward-id", quest.getItemRewardID());
		questData.setInt("item-reward-amount", quest.getItemRewardAmount());
		questData.setString("prerequisite", quest.getPrerequisite());
		questData.setString("description", quest.getDescription());
		questData.setString("completion-text", quest.getCompletionText());
	}

	/**
	 * Create a new quest file with the default values
	 * 
	 * @param questName
	 */
	public static void createQuestFile(String questName) {
		PropertyHandler newQuest = new PropertyHandler(
				"plugins/Citizens/Questers/Quests/" + questName + ".quest");
		newQuest.setString("name", "");
		newQuest.setString("type", "");
		newQuest.setString("start-npc", "");
		newQuest.setInt("iConomy-reward", 10);
		newQuest.setInt("item-reward-id", 1);
		newQuest.setInt("item-reward-amount", 10);
		newQuest.setString("prerequisite", "");
		newQuest.setString("description", "");
		newQuest.setString("completion-text", "");
	}
}
