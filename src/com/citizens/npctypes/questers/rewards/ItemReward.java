package com.citizens.npctypes.questers.rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.economy.ItemInterface;
import com.citizens.economy.Payment;
import com.citizens.npctypes.questers.Reward;
import com.citizens.npctypes.questers.quests.QuestManager.RewardType;

public class ItemReward implements Reward {
	private final ItemStack reward;
	private final boolean take;

	public ItemReward(ItemStack reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player) {
		if (this.take) {
			// dostuff
		} else {
			player.getWorld().dropItemNaturally(player.getLocation(), reward);
		}
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

	@Override
	public boolean canTake(Player player) {
		return take ? ItemInterface.hasEnough(new Payment(reward), player)
				: true;
	}
}