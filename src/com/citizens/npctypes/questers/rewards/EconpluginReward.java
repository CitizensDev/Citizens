package com.citizens.npctypes.questers.rewards;

import org.bukkit.entity.Player;

import com.citizens.economy.EconomyHandler;
import com.citizens.economy.ServerEconomyInterface;
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
		if (EconomyHandler.useEconPlugin()) {
			if (this.take) {
				ServerEconomyInterface.subtract(player.getName(), reward);
			} else {
				ServerEconomyInterface.add(player.getName(), reward);
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
}