package com.fullwall.Citizens.Questers;

import com.fullwall.Citizens.Questers.QuestManager.QuestType;

public interface CompletedQuest {
	public QuestType getType();

	public String getName();
}
