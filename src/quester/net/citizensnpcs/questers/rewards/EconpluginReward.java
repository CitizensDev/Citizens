package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EconpluginReward implements Reward {
	private final double reward;
	private final boolean take;

	public EconpluginReward(double reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player, HumanNPC npc) {
		if (EconomyManager.useEconPlugin()) {
			if (this.take) {
				EconomyManager.subtract(player.getName(), reward);
			} else {
				EconomyManager.add(player.getName(), reward);
			}
		}
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean canTake(Player player) {
		return EconomyManager.getBalance(player.getName()) - reward >= 0;
	}

	@Override
	public String getRequiredText(Player player) {
		return ChatColor.GRAY
				+ "You need "
				+ StringUtils.wrap(
						EconomyManager.format(reward
								- EconomyManager.getBalance(player.getName())),
						ChatColor.GRAY) + " more.";
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setDouble(root + ".money", reward);
	}

	public static class EconpluginRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new EconpluginReward(storage.getDouble(root + ".money"),
					take);
		}
	}
}