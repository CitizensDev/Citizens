package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RankReward implements Requirement, Reward {
	private final String reward;
	private final boolean take;
	private final boolean replace;
	private final String with;

	RankReward(String reward, String with, boolean replace, boolean take) {
		this.reward = reward;
		this.replace = replace;
		this.take = take;
		this.with = with;
	}

	@Override
	public boolean fulfilsRequirement(Player player) {
		return PermissionManager.hasRank(player, reward);
	}

	@Override
	public String getRequiredText(Player player) {
		return ChatColor.GRAY + "You must be in the group "
				+ StringUtils.wrap(reward, ChatColor.GRAY) + ".";
	}

	@Override
	public void grant(Player player, int UID) {
		if (replace && !with.isEmpty()) {
			PermissionManager.removeRank(player, reward);
			PermissionManager.grantRank(player, with, false);
		} else {
			if (replace)
				PermissionManager.setRank(player, reward);
			else
				PermissionManager.grantRank(player, reward, take);
		}
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public void save(DataKey root) {
		root.setString("rank", reward);
		root.setBoolean("replace", replace);
		root.setString("with", with);
	}

	public static class RankRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(DataKey root, boolean take) {
			return new RankReward(root.getString("rank"),
					root.getString("with"), root.getBoolean("replace", false),
					take);
		}
	}
}