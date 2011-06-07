package com.fullwall.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCTypes.Questers.Reward;

public class QuestReward implements Reward {
	private final String reward;

	public QuestReward(String quest) {
		this.reward = quest;
	}

	@Override
	public void grant(Player player) {
		// TODO: quest giving code here.
	}
}