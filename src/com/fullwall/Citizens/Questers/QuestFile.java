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
		return questData.getString("name");
	}

	public void saveName(String name) {
		this.name = name;
		questData.setString("name", name);
	}

	public String getStartNPC() {
		return questData.getString("start-NPC");
	}

	public void saveStartNPC(String startNPC) {
		this.startNPC = startNPC;
		questData.setString("start-npc", startNPC);
	}

	public String getReward() {
		return questData.getString("reward");
	}

	public void saveReward(String reward) {
		this.reward = reward;
		questData.setString("reward", reward);
	}

	public String getType() {
		return questData.getString("type");
	}

	public void saveType(String type) {
		this.type = type;
		questData.setString("type", type);
	}

	public String getQuestPrerequisite() {
		return questData.getString("quest-prerequisite");
	}

	public void saveQuestPrerequisite(String questPrerequisite) {
		this.questPrerequisite = questPrerequisite;
		questData.setString("quest-prerequisite", questPrerequisite);
	}

	public String getRankPrerequisite() {
		return questData.getString("rank-prerequisite");
	}

	public void saveRankPrerequisite(String rankPrerequisite) {
		this.rankPrerequisite = rankPrerequisite;
		questData.setString("rank-prerequisite", rankPrerequisite);
	}

	public String getDescription() {
		return questData.getString("description");
	}

	public void saveDescription(String description) {
		this.description = description;
		questData.setString("description", description);
	}

	public String getCompletionText() {
		return questData.getString("completion-text");
	}

	public void saveCompletionText(String completionText) {
		this.completionText = completionText;
		questData.setString("completionText", completionText);
	}
}