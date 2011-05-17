package com.fullwall.Citizens.Questers;

import java.util.ArrayList;

public class PlayerProfile {
	private ArrayList<CompletedQuest> completedQuests = new ArrayList<CompletedQuest>();
	private Quest currentQuest;

	public void setCompletedQuests(ArrayList<CompletedQuest> completedQuests) {
		this.completedQuests = completedQuests;
	}

	public ArrayList<CompletedQuest> getCompletedQuests() {
		return completedQuests;
	}

	public void setCurrentQuest(Quest currentQuest) {
		this.currentQuest = currentQuest;
	}

	public Quest getCurrentQuest() {
		return currentQuest;
	}
}