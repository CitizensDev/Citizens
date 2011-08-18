package net.citizensnpcs.questers.quests;

import java.util.concurrent.TimeUnit;

public class CompletedQuest {
	private final int npcID;
	private final String name;
	private final long elapsed, seconds, minutes, hours;
	private int timesCompleted;

	public CompletedQuest(String quest, int npcID, int completed, long elapsed) {
		this.elapsed = elapsed;
		this.timesCompleted = completed;
		this.minutes = TimeUnit.MINUTES.convert(elapsed, TimeUnit.MILLISECONDS);
		this.seconds = TimeUnit.SECONDS.convert(elapsed, TimeUnit.MILLISECONDS);
		this.hours = TimeUnit.HOURS.convert(elapsed, TimeUnit.MILLISECONDS);
		this.name = quest;
		this.npcID = npcID;
	}

	public String getName() {
		return this.name;
	}

	public int getQuesterUID() {
		return npcID;
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

	public long getElapsed() {
		return elapsed;
	}

	public int getTimesCompleted() {
		return timesCompleted;
	}

	public void setTimeCompleted(int completed) {
		this.timesCompleted = completed;
	}
}