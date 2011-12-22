package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.economy.Economy;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EconpluginReward implements Requirement, Reward {
	private final double reward;
	private final boolean take;

	EconpluginReward(double reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player, int UID) {
		if (!Economy.useEconPlugin())
			return;
		if (this.take) {
			Economy.getAccount(player).subtract(reward);
		} else {
			Economy.getAccount(player).add(reward);
		}
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean fulfilsRequirement(Player player) {
		return Economy.getAccount(player).hasEnough(reward);
	}

	@Override
	public String getRequiredText(Player player) {
		return ChatColor.GRAY
				+ "You need "
				+ StringUtils.wrap(
						Economy.format(reward
								- Economy.getAccount(player).balance()),
						ChatColor.GRAY) + " more.";
	}

	@Override
	public void save(DataKey root) {
		root.setDouble("money", reward);
	}

	public static class EconpluginRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(DataKey root, boolean take) {
			return new EconpluginReward(root.getDouble("money"), take);
		}
	}
}