package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.RewardBuilder;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HealthReward implements Reward {
	private final int reward;
	private final boolean take;

	public HealthReward(int reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player, HumanNPC npc) {
		if (this.take)
			player.setHealth(player.getHealth() - reward);
		else
			player.setHealth(player.getHealth() + reward);
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean canTake(Player player) {
		return take ? player.getHealth() - reward > 0 : true;
	}

	@Override
	public String getRequiredText(Player player) {
		return ChatColor.GRAY + "You need "
				+ StringUtils.wrap(player.getHealth() - reward, ChatColor.GRAY)
				+ " more health.";
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setInt(root + ".amount", reward);
	}

	public static class HealthRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new HealthReward(storage.getInt(root + ".amount"), take);
		}
	}
}