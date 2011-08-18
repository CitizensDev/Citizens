package net.citizensnpcs.questers.quests;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.questers.Reward;

public class Quest {
	private String questName = "";
	private String completionText = "";
	private String description = "";
	private String acceptanceText = "";
	private final List<Reward> rewards = new ArrayList<Reward>();
	private final List<Reward> requirements = new ArrayList<Reward>();
	private Objectives objectives;
	private int repeatLimit = -1;

	public Quest(String name) {
		this.questName = name;
	}

	// Get the name of a quest
	public String getName() {
		return questName;
	}

	// Add a reward
	public void addReward(Reward reward) {
		rewards.add(reward);
	}

	// Set the name of a quest
	public void setName(String questName) {
		this.questName = questName;
	}

	public List<Reward> getRewards() {
		return this.rewards;
	}

	// Get the description of a quest
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// Get the text to be outputted on completion of a quest
	public String getCompletedText() {
		return completionText;
	}

	public void setCompletedText(String text) {
		this.completionText = text;
	}

	public void addObjective(QuestStep step) {
		this.objectives.add(step);
	}

	public Objectives getObjectives() {
		return objectives;
	}

	public void setObjectives(Objectives objectives) {
		this.objectives = objectives;
	}

	public List<Reward> getRequirements() {
		return requirements;
	}

	public void addRequirement(Reward reward) {
		this.requirements.add(reward);
	}

	public String getAcceptanceText() {
		return acceptanceText;
	}

	public void setAcceptanceText(String text) {
		this.acceptanceText = text;
	}

	public void setRepeatLimit(int repeatLimit) {
		this.repeatLimit = repeatLimit;
	}

	public int getRepeatLimit() {
		return this.repeatLimit;
	}
}