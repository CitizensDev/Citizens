package com.fullwall.Citizens.Questers;

import com.fullwall.Citizens.PropertyHandler;

public class QuestPropertyPool {

	public static void saveQuest(Quest quest) {
		PropertyHandler questData = new PropertyHandler("plugins/Citizens/Questers/Quests/" + quest.getName() + ".quest");
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
}
