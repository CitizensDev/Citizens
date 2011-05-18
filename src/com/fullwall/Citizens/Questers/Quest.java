package com.fullwall.Citizens.Questers;

import org.bukkit.event.Event;

public interface Quest extends CompletedQuest {
	public String getDescription();

	public String getCompletedText();

	public boolean isCompleted();

	public void updateProgress(Event event);

	public PlayerProfile getPlayerProfile();
}