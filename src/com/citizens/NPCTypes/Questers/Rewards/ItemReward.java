package com.citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.Economy.EconomyHandler;
import com.citizens.Economy.Payment;
import com.citizens.NPCTypes.Questers.Reward;
import com.citizens.NPCTypes.Questers.Quests.QuestManager.RewardType;

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

	@Override
	public RewardType getType() {
		return RewardType.ITEM;
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