package com.fullwall.Citizens.NPCTypes.Questers;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public abstract class Quest implements CompletedQuest {
	protected HumanNPC quester;
	protected Player player;
	protected String questName = "";
	protected String completionText = "";
	protected String description = "";
	protected boolean completed = false;
	protected ArrayList<Reward> rewards = new ArrayList<Reward>();

	public Quest(HumanNPC quester, Player player) {
		this.quester = quester;
		this.player = player;
	}

	public HumanNPC getQuester() {
		return quester;
	}

	public Player getPlayer() {
		return player;
	}

	public String getName() {
		return questName;
	}

	public void addReward(Reward reward) {
		rewards.add(reward);
	}

	public void setName(String questName) {
		this.questName = questName;
	}

	public String getDescription() {
		return description;
	}

	public String getCompletedText() {
		return completionText;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void updateProgress(Event event) {
		if (isCompleted())
			player.sendMessage(getCompletedText());
	}

}