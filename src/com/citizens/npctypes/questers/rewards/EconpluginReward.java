package com.citizens.npctypes.questers.rewards;

import org.bukkit.entity.Player;

import com.citizens.economy.EconomyManager;
import com.citizens.npctypes.questers.Reward;
import com.citizens.npctypes.questers.quests.QuestManager.RewardType;

public class EconpluginReward implements Reward {
	private final double reward;
	private final boolean take;

	public EconpluginReward(double reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player) {
		if (EconomyManager.useEconPlugin()) {
			if (this.take) {
				EconomyManager.subtract(player.getName(), reward);
			} else {
				EconomyManager.add(player.getName(), reward);
			}
		}

	}

	@Override
	public RewardType getType() {
		return RewardType.MONEY;
	}

	@Override
	public Object getReward() {
		return reward;
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean canTake(Player player) {
		return take ? EconomyManager.getBalance(player.getName()) - reward >= 0
				: true;
	}
}