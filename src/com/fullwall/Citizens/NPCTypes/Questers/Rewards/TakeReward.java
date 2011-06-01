package com.fullwall.Citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.Payment;
import com.fullwall.Citizens.NPCTypes.Questers.Reward;

public class TakeReward implements Reward {
	private final ItemStack[] take;

	public TakeReward(ItemStack... take) {
		this.take = take;
	}

	@Override
	public void grant(Player player) {
		for (ItemStack pay : take) {
			EconomyHandler.pay(new Payment(pay), player, player.getInventory()
					.getHeldItemSlot());
		}
	}
}
