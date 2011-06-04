package com.fullwall.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.ServerEconomyInterface;
import com.fullwall.Citizens.NPCTypes.Questers.Reward;

public class EconpluginReward implements Reward {
	private final double reward;
	private final boolean take;

	public EconpluginReward(double reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player) {
		if (EconomyHandler.useIconomy()) {
			if (this.take)
				ServerEconomyInterface.subtract(player.getName(), reward);
			else
				ServerEconomyInterface.add(player.getName(), reward);
		}

	}
}