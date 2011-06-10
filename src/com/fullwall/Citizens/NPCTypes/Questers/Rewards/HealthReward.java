package com.fullwall.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCTypes.Questers.Reward;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager.RewardType;

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

	@Override
	public RewardType getType() {
		return RewardType.HEALTH;
	}

	@Override
	public Object getReward() {
		return reward;
	}

	@Override
	public boolean isTake() {
		return take;
	}
}