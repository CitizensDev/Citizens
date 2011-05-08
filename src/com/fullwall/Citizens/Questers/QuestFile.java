package com.fullwall.Citizens.Questers;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuestFile {
	private PropertyHandler questFile;
	private String questName;
	private HumanNPC startNPC;

	public QuestFile(String questName, HumanNPC startNPC) {
		this.questName = questName;
		this.startNPC = startNPC;
		this.questFile = new PropertyHandler("plugins/Citizens/Questers/Quests/" + questName + ".quests");
		loadQuest(this.questFile, questName);
	}
	
	public void loadQuest(PropertyHandler questFile, String questName) {
		this.questFile = questFile;
		this.questName = questName;
		if (!questFile.keyExists("quest-name")) {
			questFile.setString("quest-name", questName);
		}
		if (!questFile.keyExists("start-NPC")) {
			questFile.setString("start-NPC", startNPC.getStrippedName());
		}
		if (!questFile.keyExists("iConomy-reward")) {
			questFile.setDouble("iConomy-reward", 10);
		}
		if (!questFile.keyExists("item-reward-id")) {
			questFile.setDouble("item-reward-id", 322);
		}
		if (!questFile.keyExists("item-reward-amount")) {
			questFile.setDouble("item-reward-amount", 1);
		}
	}
	
	public PropertyHandler getQuestFile() {
		return questFile;
	}
	
	public String getQuestName() {
		return questName;
	}
	
	public HumanNPC getStartNPC() {
		return startNPC;
	}
}
