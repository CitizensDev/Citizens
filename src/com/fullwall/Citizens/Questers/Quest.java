package com.fullwall.Citizens.Questers;

public class Quest {
	private String name;
	private String startNPC;
	private String type;
	private String reward;
	private String questPrerequisite;
	private String rankPrerequisite;
	private String description;
	private String completionText;
	private boolean isCurrentQuest;

	public Quest(String name, String startNPC, String type, String reward,
			String questPrerequisite, String rankPrerequisite,
			String description, String completionText) {
		this.name = name;
		this.startNPC = startNPC;
		this.type = type;
		this.reward = reward;
		this.questPrerequisite = questPrerequisite;
		this.rankPrerequisite = rankPrerequisite;
		this.description = description;
		this.completionText = completionText;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartNPC() {
		return startNPC;
	}

	public void setStartNPC(String startNPC) {
		this.startNPC = startNPC;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getQuestPrerequisite() {
		return questPrerequisite;
	}

	public void setQuestPrerequisite(String questPrerequisite) {
		this.questPrerequisite = questPrerequisite;
	}

	public String getRankPrerequisite() {
		return rankPrerequisite;
	}

	public void setRankPrerequisite(String rankPrerequisite) {
		this.rankPrerequisite = rankPrerequisite;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCompletionText() {
		return completionText;
	}

	public void setCompletionText(String completionText) {
		this.completionText = completionText;
	}

	public boolean isCurrentQuest() {
		return isCurrentQuest;
	}

	public void setCurrentQuest(boolean state) {
		this.isCurrentQuest = state;
	}
}