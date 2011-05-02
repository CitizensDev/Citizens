package com.fullwall.Citizens.Questers;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public interface Quest {

	// returns the NPC who gave the quest
	public HumanNPC getQuestGiver();
	
	// sets the NPC who gives a quest
	public void setQuestGiver(HumanNPC questGiver);
	
	// returns player involved in quest
	public Player getPlayer();

	// returns type of quest
	public QuestType getType();
	
	// returns the goal of the quest
	public Goal getGoal();
	
	// sets the goal of the quest
	public void setGoal(Goal goal);

	// returns the progress of a quest
	public String getProgress();

	// returns true if quest has been completed
	public boolean isCompleted();
}
