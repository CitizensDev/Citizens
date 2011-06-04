package com.fullwall.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCTypes.Questers.Reward;

public class HealthReward implements Reward {
	private final int reward;
	private final boolean take;

	public HealthReward(int reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player) {
		if (this.take)
			player.setHealth(player.getHealth() - reward);
		else
			player.setHealth(player.getHealth() + reward);
	}
}