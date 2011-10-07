package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.data.PlayerProfile;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class QuestReward implements Reward {
	private final String reward;
	private final boolean take;
	private final int times;

	QuestReward(String quest, int times, boolean take) {
		this.reward = quest;
		this.take = take;
		this.times = times;
	}

	@Override
	public void grant(Player player, int UID) {
		if (!take)
			QuestManager.assignQuest(player, UID, reward);
		else if (PlayerProfile.getProfile(player.getName()).getQuest()
				.equalsIgnoreCase(reward))
			PlayerProfile.getProfile(player.getName()).setProgress(null);
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean canTake(Player player) {
		return times > 1 ? PlayerProfile.getProfile(player.getName())
				.getCompletedTimes(reward) >= times : PlayerProfile.getProfile(
				player.getName()).hasCompleted(reward);
	}

	@Override
	public String getRequiredText(Player player) {
		return ChatColor.GRAY + "You must have completed the quest "
				+ StringUtils.wrap(reward, ChatColor.GRAY) + ".";
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setString(root + ".quest", reward);
	}

	public static class QuestRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new QuestReward(storage.getString(root + ".quest"),
					storage.getInt(root + ".times"), take);
		}
	}
}