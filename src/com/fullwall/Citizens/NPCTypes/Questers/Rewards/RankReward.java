package com.fullwall.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.NPCTypes.Questers.Reward;

public class RankReward implements Reward {

	private final String reward;

	public RankReward(String reward) {
		this.reward = reward;
	}

	@Override
	public void grant(Player player) {
		// TODO - look into 3.0 API and finish grantRank()
		Permission.grantRank(player, reward);
	}
}