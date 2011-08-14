package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.RewardBuilder;
import net.citizensnpcs.questers.quests.QuestManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

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
		return true;
	}

	@Override
	public String getRequiredText(Player player) {
		return "";
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