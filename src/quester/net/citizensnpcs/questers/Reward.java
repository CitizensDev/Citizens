package net.citizensnpcs.questers;

import net.citizensnpcs.questers.quests.QuestManager.RewardType;

import org.bukkit.entity.Player;

public interface Reward {
	public void grant(Player player);

	public boolean canTake(Player player);

	public RewardType getType();

	public Object getReward();

	public boolean isTake();
}