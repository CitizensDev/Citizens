package com.fullwall.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Economy.ServerEconomyInterface;
import com.fullwall.Citizens.NPCTypes.Questers.Reward;

public class EconpluginReward implements Reward {
	private double reward;

	public EconpluginReward(double reward) {
		this.reward = reward;
	}

	@Override
	public void grant(Player player) {
		ServerEconomyInterface.add(player.getName(), reward);
	}
}
