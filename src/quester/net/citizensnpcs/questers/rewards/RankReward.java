package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.RewardBuilder;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;

public class RankReward implements Reward {
	private final String reward;

	public RankReward(String reward) {
		this.reward = reward;
	}

	@Override
	public void grant(Player player, HumanNPC npc) {
		// TODO - look into 3.0 API and finish grantRank()
		PermissionManager.grantRank(player, reward);
	}

	@Override
	public boolean isTake() {
		return false;
	}

	@Override
	public boolean canTake(Player player) {
		return true;
	}

	@Override
	public String getRequiredText(Player player) {
		return "";
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setString(root + ".rank", reward);
	}

	public static class RankRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new RankReward(storage.getString(root + ".rank"));
		}
	}
}