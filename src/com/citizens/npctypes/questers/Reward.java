package com.citizens.npctypes.questers;

import org.bukkit.entity.Player;

import com.citizens.npctypes.questers.quests.QuestManager.RewardType;

public interface Reward {
	public void grant(Player player);

	public boolean canTake(Player player);

	public RewardType getType();

	public Object getReward();

	public boolean isTake();
}