package com.fullwall.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.Payment;
import com.fullwall.Citizens.NPCTypes.Questers.Reward;

public class ItemReward implements Reward {
	private final ItemStack reward;
	private final boolean take;

	public ItemReward(ItemStack reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player) {
		if (this.take)
			EconomyHandler.pay(new Payment(reward), player, player
					.getInventory().getHeldItemSlot());
		else
			player.getWorld().dropItemNaturally(player.getLocation(), reward);
	}
}