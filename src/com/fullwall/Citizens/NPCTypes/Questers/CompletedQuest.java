package com.fullwall.Citizens.NPCTypes.Questers;

import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;

public interface CompletedQuest {
	public QuestType getType();

	public String getName();

	public void setName(String name);
}