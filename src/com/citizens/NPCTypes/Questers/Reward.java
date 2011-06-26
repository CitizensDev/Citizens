package com.citizens.NPCTypes.Questers;

import org.bukkit.entity.Player;

import com.citizens.NPCTypes.Questers.Quests.QuestManager.RewardType;

public interface Reward {
	public void grant(Player player);

	public RewardType getType();

	public Object getReward();

	public boolean isTake();
}