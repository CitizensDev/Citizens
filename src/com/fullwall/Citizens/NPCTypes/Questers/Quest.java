package com.fullwall.Citizens.NPCTypes.Questers;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public abstract class Quest implements CompletedQuest {
	protected HumanNPC quester;
	protected Player player;
	protected String questName = "";
	protected String completionText = "";
	protected String description = "";
	protected boolean completed = false;
	protected ArrayList<Reward> rewards = new ArrayList<Reward>();

	public Quest(HumanNPC quester, Player player) {
		this.quester = quester;
		this.player = player;
	}

	/**
	 * Get the quester assigned to a quest
	 * 
	 * @return
	 */
	public HumanNPC getQuester() {
		return quester;
	}

	/**
	 * Get the player doing the questing
	 * 
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Get the name of a quest
	 * 
	 * @return
	 */
	public String getName() {
		return questName;
	}

	/**
	 * Add a reward
	 * 
	 * @param reward
	 */
	public void addReward(Reward reward) {
		rewards.add(reward);
	}

	/**
	 * Set the name of a quest
	 * 
	 * @param questName
	 */
	public void setName(String questName) {
		this.questName = questName;
	}

	/**
	 * Get the description of a quest
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the text to be outputted on completion of a quest
	 * 
	 * @return
	 */
	public String getCompletedText() {
		return completionText;
	}

	/**
	 * Get whether a quest is completed
	 * 
	 * @return
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * Update the progress of a quest
	 * 
	 * @param event
	 */
	public void updateProgress(Event event) {
		if (isCompleted())
			player.sendMessage(getCompletedText());
	}
}