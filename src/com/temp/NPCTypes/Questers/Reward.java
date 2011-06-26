package com.temp.NPCTypes.Questers;

import org.bukkit.entity.Player;

import com.temp.NPCTypes.Questers.Quests.QuestManager.RewardType;

public interface Reward {
	public void grant(Player player);

	public RewardType getType();

	public Object getReward();

	public boolean isTake();
}