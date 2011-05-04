package com.fullwall.Citizens.Questers;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public interface Quest {

	/**
	 * 
	 * @return the NPC who gives out a quest
	 */
	public HumanNPC getQuestGiver();

	/**
	 * 
	 * @param questGiver the NPC who gives out a quest
	 */
	public void setQuestGiver(HumanNPC questGiver);

	/**
	 * 
	 * @return the player who is doing the questing
	 */
	public Player getPlayer();

	/**
	 * 
	 * @return the type of quest it is
	 */
	public QuestType getType();

	/**
	 * 
	 * @return the progress of an active quest
	 */
	public String getProgress();

	/**
	 * 
	 * @return true if a quest is complete
	 */
	public boolean isCompleted();
}
