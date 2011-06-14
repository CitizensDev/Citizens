package com.fullwall.Citizens.NPCTypes.Questers;

import java.util.ArrayList;
import java.util.List;

import com.fullwall.Citizens.NPCTypes.Questers.Objectives.Objective;
import com.fullwall.Citizens.NPCTypes.Questers.Objectives.Objectives;

public class Quest {
	private String questName = "";
	private String completionText = "";
	private String description = "";
	private final List<Reward> rewards = new ArrayList<Reward>();
	private Objectives objectives;

	public Quest(String name) {
		this.questName = name;
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

	public List<Reward> getRewards() {
		return this.rewards;
	}

	/**
	 * Get the description of a quest
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the text to be outputted on completion of a quest
	 * 
	 * @return
	 */
	public String getCompletedText() {
		return completionText;
	}

	public void setCompletedText(String text) {
		this.completionText = text;
	}

	public void addObjective(Objective objective) {
		this.objectives.add(objective);
	}

	public Objectives getObjectives() {
		return objectives;
	}

	public void setObjectives(Objectives objectives) {
		this.objectives = objectives;
	}
}