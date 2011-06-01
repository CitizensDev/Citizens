package com.fullwall.Citizens.NPCTypes.Questers;

import java.util.ArrayList;

public class PlayerProfile {
	private StoredProfile profile;
	private int rank;
	private ArrayList<CompletedQuest> completedQuests = new ArrayList<CompletedQuest>();
	private Quest currentQuest;
	private QuestProgress progress;

	public PlayerProfile(String name) {
		profile = new StoredProfile(name);
		rank = 1;
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

	public QuestProgress getProgress() {
		return progress;
	}

	public void setProgress(QuestProgress progress) {
		this.progress = progress;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}

	public boolean hasQuest() {
		return currentQuest == null;
	}

	public void save() {
		this.profile.save();
	}
}