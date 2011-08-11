package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.quests.QuestManager.RewardType;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PermissionReward implements Reward {
	private final String reward;
	private final boolean take;

	public PermissionReward(String reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player) {
		if (take)
			PermissionManager.givePermission(player, reward, true);
		else
			PermissionManager.givePermission(player, reward, false);
	}

	@Override
	public RewardType getType() {
		return RewardType.PERMISSION;
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
		return take ? PermissionManager.generic(player, reward) : true;
	}

	@Override
	public String getRequiredText(Player player) {
		return ChatColor.GRAY + "You don't have the necessary permissions.";
	}
}