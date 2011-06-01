package com.fullwall.Citizens.NPCTypes.Questers;

import java.util.ArrayList;
import java.util.List;

import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;

public class Quest implements CompletedQuest {
	private String questName = "";
	private String completionText = "";
	private String description = "";
	private final List<Reward> rewards = new ArrayList<Reward>();
	private Objective objective;
	private QuestType type;

	public Quest(String name, QuestType type) {
		this.questName = name;
		this.type = type;
	}

	/**
	 * Get the name of a quest
	 * 
	 * @return
	 */
	@Override
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
	@Override
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

	public void setObjective(Objective objective) {
		this.objective = objective;
	}

	public Objective getObjective() {
		return objective;
	}

	public void setType(QuestType type) {
		this.type = type;
	}

	@Override
	public QuestType getType() {
		return this.type;
	}
}