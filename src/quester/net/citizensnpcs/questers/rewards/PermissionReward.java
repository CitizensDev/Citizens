package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.Storage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PermissionReward implements Reward {
	private final String reward;
	private final boolean take;

	PermissionReward(String reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player, int UID) {
		PermissionManager.givePermission(player, reward, take);
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean fulfilsRequirement(Player player) {
		return PermissionManager.hasPermission(player, reward);
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