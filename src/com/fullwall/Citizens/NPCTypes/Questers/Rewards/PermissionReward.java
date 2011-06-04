package com.fullwall.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.NPCTypes.Questers.Reward;

public class PermissionReward implements Reward {
	private final String reward;

	public PermissionReward(String reward) {
		this.reward = reward;
	}

	@Override
	public void grant(Player player) {
		Permission.givePermission(player, reward);
	}

}
