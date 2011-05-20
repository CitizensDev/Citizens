package com.fullwall.Citizens.NPCTypes.Questers;

import java.util.List;

import org.bukkit.event.Event;

public interface Quest extends CompletedQuest {
	public String getDescription();

	public String getCompletedText();

	public boolean isCompleted();

	public void updateProgress(Event event);

	public List<Reward> getRewards();

	public void addReward(Reward reward);

	public PlayerProfile getPlayerProfile();
}