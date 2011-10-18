package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.utils.LocationUtils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportReward implements Reward {
	private final Location reward;

	TeleportReward(Location reward) {
		this.reward = reward;
	}

	@Override
	public void grant(Player player, int UID) {
		player.teleport(reward);
	}

	@Override
	public boolean isTake() {
		return false;
	}

	@Override
	public void save(Storage storage, String root) {
		LocationUtils.saveLocation(storage, reward, root, false);
	}

	public static class TeleportRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new TeleportReward(LocationUtils.loadLocation(storage, root,
					false));
		}
	}
}
