package com.fullwall.Citizens.Questers;

public interface Quest extends CompletedQuest {
	public String getDescription();

	public String getCompletedText();

	public boolean isCompleted();

	public void updateProgress();
}