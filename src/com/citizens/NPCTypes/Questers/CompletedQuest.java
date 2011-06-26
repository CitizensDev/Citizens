package com.citizens.NPCTypes.Questers;

import com.citizens.NPCTypes.Questers.Quest;

public class CompletedQuest {
	private final String npcName;
	private final String name;
	private final long seconds, minutes, hours;

	public CompletedQuest(Quest quest, String npcName, long elapsed) {
		this.minutes = elapsed / 60000;
		this.seconds = elapsed / 1000;
		this.hours = elapsed / 3600000;
		this.name = quest.getName();
		this.npcName = npcName;
	}

	public String getName() {
		return this.name;
	}

	public String getQuesterName() {
		return npcName;
	}

	public long getSeconds() {
		return seconds;
	}

	public long getMinutes() {
		return minutes;
	}

	public long getHours() {
		return hours;
	}
}