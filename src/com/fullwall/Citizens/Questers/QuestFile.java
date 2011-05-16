package com.fullwall.Citizens.Questers;

import com.fullwall.Citizens.PropertyHandler;

public class QuestFile {

	private PropertyHandler questData;
	private String name;
	private String startNPC;
	private String type;
	private String reward;
	private String questPrerequisite;
	private String rankPrerequisite;
	private String description;
	private String completionText;

	public QuestFile(Quest quest) {
		questData = new PropertyHandler("plugins/Citizens/Questers/Quests/"
				+ quest.getName() + ".quest");
		saveName(quest.getName());
		saveStartNPC(quest.getStartNPC());
	}

	public String getName() {
		this.name = questData.getString("name");
		return name;
	}

	public void saveName(String name) {
		this.name = name;
		questData.setString("name", name);
	}

	public String getStartNPC() {
		this.startNPC = questData.getString("start-NPC");
		return startNPC;
	}

	public void saveStartNPC(String startNPC) {
		this.startNPC = startNPC;
		questData.setString("start-npc", startNPC);
	}

	public String getReward() {
		this.reward = questData.getString("reward");
		return reward;
	}

	public void saveReward(String reward) {
		this.reward = reward;
		questData.setString("reward", reward);
	}

	public String getType() {
		this.type = questData.getString("type");
		return type;
	}

	public void saveType(String type) {
		this.type = type;
		questData.setString("type", type);
	}

	public String getQuestPrerequisite() {
		this.questPrerequisite = questData.getString("quest-prerequisite");
		return questPrerequisite;
	}

	public void saveQuestPrerequisite(String questPrerequisite) {
		this.questPrerequisite = questPrerequisite;
		questData.setString("quest-prerequisite", questPrerequisite);
	}

	public String getRankPrerequisite() {
		this.rankPrerequisite = questData.getString("rank-prerequisite");
		return rankPrerequisite;
	}

	public void saveRankPrerequisite(String rankPrerequisite) {
		this.rankPrerequisite = rankPrerequisite;
		questData.setString("rank-prerequisite", rankPrerequisite);
	}

	public String getDescription() {
		this.description = questData.getString("description");
		return description;
	}

	public void saveDescription(String description) {
		this.description = description;
		questData.setString("description", description);
	}

	public String getCompletionText() {
		this.completionText = questData.getString("completion-text");
		return completionText;
	}

	public void saveCompletionText(String completionText) {
		this.completionText = completionText;
		questData.setString("completionText", completionText);
	}
}