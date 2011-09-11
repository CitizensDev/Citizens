package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.RewardBuilder;
import net.citizensnpcs.resources.npclib.HumanNPC;

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
	public void grant(Player player, HumanNPC npc) {
		if (take) {
			PermissionManager.givePermission(player, reward, true);
		} else {
			PermissionManager.givePermission(player, reward, false);
		}
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean canTake(Player player) {
		return PermissionManager.generic(player, reward);
	}

	@Override
	public String getRequiredText(Player player) {
		return ChatColor.GRAY + "You don't have the necessary permissions.";
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setString(root + ".permission", reward);
	}

	public static class PermissionRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new PermissionReward(
					storage.getString(root + ".permission"), take);
		}
	}
}