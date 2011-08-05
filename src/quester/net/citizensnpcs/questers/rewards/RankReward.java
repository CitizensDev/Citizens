package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.Permission;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.quests.QuestManager.RewardType;

import org.bukkit.entity.Player;

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

	@Override
	public boolean canTake(Player player) {
		return true;
	}
}