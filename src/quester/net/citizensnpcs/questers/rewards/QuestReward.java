package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.data.PlayerProfile;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class QuestReward implements Requirement, Reward {
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
			new AssignQuestRunnable(player, UID, reward).schedule();
		else if (PlayerProfile.getProfile(player.getName()).getQuest()
				.equalsIgnoreCase(reward))
			PlayerProfile.getProfile(player.getName()).setProgress(null);
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean fulfilsRequirement(Player player) {
		if (times <= 0) {
			return !PlayerProfile.getProfile(player.getName()).hasCompleted(
					reward);
		}
		return PlayerProfile.getProfile(player.getName()).getCompletedTimes(
				reward) >= times;
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

	private static class AssignQuestRunnable implements Runnable {
		private final Player player;
		private final int UID;
		private final String quest;

		public AssignQuestRunnable(Player player, int UID, String quest) {
			this.player = player;
			this.UID = UID;
			this.quest = quest;
		}

		public void schedule() {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin,
					this, 1);
		}

		@Override
		public void run() {
			QuestManager.assignQuest(player, UID, quest);
		}
	}

	public static class QuestRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new QuestReward(storage.getString(root + ".quest"),
					storage.getInt(root + ".times", 1), take);
		}
	}
}