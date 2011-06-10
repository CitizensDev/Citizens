package com.fullwall.Citizens.NPCTypes.Questers;

import java.util.ArrayList;
import java.util.List;

import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestProgress;

public class PlayerProfile {
	private StoredProfile profile;
	private int rank;
	private final List<CompletedQuest> completedQuests = new ArrayList<CompletedQuest>();
	private QuestProgress progress;

	public PlayerProfile(String name) {
		profile = new StoredProfile(name);
		rank = 1;
	}

	public void setProfile(StoredProfile profile) {
		this.profile = profile;
	}

	public List<CompletedQuest> getCompletedQuests() {
		return completedQuests;
	}

	public void addCompletedQuest(CompletedQuest quest) {
		this.completedQuests.add(quest);
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
		return progress == null;
	}

	public void save() {
		this.profile.save();
	}
}