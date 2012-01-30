package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ExperienceLevelReward implements Requirement, Reward {

  private final int reward;
	private final boolean take;

	ExperienceLevelReward(int reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player, int UID) {
		player.setTotalExperience(player.getTotalExperience() + reward);
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean fulfilsRequirement(Player player) {
		return player.getTotalExperience() - reward > 0;
	}

	@Override
	public String getRequiredText(Player player) {
		return ChatColor.GRAY + "You need "
				+ StringUtils.wrap(player.getTotalExperience() - reward, ChatColor.GRAY)
				+ " more experience levels.";
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setInt(root + ".levels", reward);
	}

	public static class ExperenceLevelBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new ExperienceLevelReward(storage.getInt(root + ".levels"), take);
		}
	}
}