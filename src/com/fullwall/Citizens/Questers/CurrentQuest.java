package com.fullwall.Citizens.Questers;

public class CurrentQuest {
	private Quest quest;
	private String name;
	private String startNPC;
	private String type;
	private String reward;
	private String questPrerequisite;
	private String rankPrerequisite;
	private String description;
	private String completionText;

	public CurrentQuest(Quest quest) {
		this.quest = quest;
		this.name = quest.getName();
		this.startNPC = quest.getStartNPC();
		this.type = quest.getType();
		this.reward = quest.getReward();
		this.questPrerequisite = quest.getQuestPrerequisite();
		this.rankPrerequisite = quest.getRankPrerequisite();
		this.description = quest.getDescription();
		this.completionText = quest.getCompletionText();
		quest.setCurrentQuest(true);
	}
}