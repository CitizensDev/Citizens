package com.fullwall.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCTypes.Questers.Reward;

public class HealthReward implements Reward {
	private int reward;

	public HealthReward(int reward) {
		this.reward = reward;
	}

	@Override
	public void grant(Player player) {
		player.setHealth(player.getHealth() + reward);
	}
}
