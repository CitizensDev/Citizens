package com.fullwall.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.NPCTypes.Questers.Reward;

public class ItemReward implements Reward {
	private final ItemStack[] reward;

	public ItemReward(ItemStack... reward) {
		this.reward = reward;
	}

	@Override
	public void grant(Player player) {
		for (ItemStack give : reward) {
			player.getWorld().dropItemNaturally(player.getLocation(), give);
		}
	}
}