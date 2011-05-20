package com.fullwall.Citizens.NPCTypes.Questers;

import java.util.ArrayList;

public class PlayerProfile {
	private StoredProfile profile;
	private ArrayList<CompletedQuest> completedQuests = new ArrayList<CompletedQuest>();
	private Quest currentQuest = null;

	public PlayerProfile(String name) {
		profile = new StoredProfile(name);
	}

	public void setProfile(StoredProfile profile) {
		this.profile = profile;
	}

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

	public boolean hasQuest() {
		return currentQuest == null;
	}
}