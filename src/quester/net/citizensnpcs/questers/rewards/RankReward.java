package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.RewardBuilder;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RankReward implements Reward {
	private final String reward;
	private final boolean take;

	public RankReward(String reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player, HumanNPC npc) {
		PermissionManager.grantRank(player, reward, take);
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean canTake(Player player) {
		return PermissionManager.hasRank(player, reward);
	}

	@Override
	public String getRequiredText(Player player) {
		return ChatColor.GRAY + "You must be in the group "
				+ StringUtils.wrap(reward, ChatColor.GRAY) + ".";
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setString(root + ".rank", reward);
	}

	public static class RankRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new RankReward(storage.getString(root + ".rank"), take);
		}
	}
}