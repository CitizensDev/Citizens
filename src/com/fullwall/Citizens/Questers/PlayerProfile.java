package com.fullwall.Citizens.Questers;

import java.util.ArrayList;

import org.bukkit.event.Event;

public class PlayerProfile {
	private ArrayList<CompletedQuest> completedQuests = new ArrayList<CompletedQuest>();
	private Quest currentQuest;
	private String name;

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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void get(Event et) {

	}
}