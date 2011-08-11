package net.citizensnpcs.questers;

import net.citizensnpcs.questers.quests.QuestManager.RewardType;

import org.bukkit.entity.Player;

public interface Reward {
	public void grant(Player player);

	public boolean canTake(Player player);

	public String getRequiredText(Player player);

	public boolean isTake();

	public RewardType getType();

	public Object getReward();
}