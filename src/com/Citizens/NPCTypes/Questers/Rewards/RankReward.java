package com.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;

import com.Citizens.Permission;
import com.Citizens.NPCTypes.Questers.Reward;
import com.Citizens.NPCTypes.Questers.Quests.QuestManager.RewardType;

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