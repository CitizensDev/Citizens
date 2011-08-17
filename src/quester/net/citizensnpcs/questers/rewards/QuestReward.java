package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.PlayerProfile;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.RewardBuilder;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class QuestReward implements Reward {
	private final String reward;

	public QuestReward(String quest) {
		this.reward = quest;
	}

	@Override
	public void grant(Player player, HumanNPC npc) {
		QuestManager.assignQuest(npc, player, reward);
	}

	@Override
	public boolean isTake() {
		return false;
	}

	@Override
	public boolean canTake(Player player) {
		return PlayerProfile.getProfile(player.getName()).hasCompleted(reward);
	}

	@Override
	public String getRequiredText(Player player) {
		return ChatColor.GRAY + "You must have completed the quest "
				+ StringUtils.wrap(reward) + ".";
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setString(root + ".quest", reward);
	}

	public static class QuestRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new QuestReward(storage.getString(root + ".quest"));
		}
	}
}