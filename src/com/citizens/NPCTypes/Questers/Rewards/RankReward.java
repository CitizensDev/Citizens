package com.citizens.npctypes.questers.rewards;

import org.bukkit.entity.Player;

import com.citizens.Permission;
import com.citizens.npctypes.questers.Reward;
import com.citizens.npctypes.questers.quests.QuestManager.RewardType;

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

	@Override
	public RewardType getType() {
		return RewardType.RANK;
	}

	@Override
	public Object getReward() {
		return reward;
	}

	@Override
	public boolean isTake() {
		return false;
	}
}