package com.citizens.npctypes.questers.rewards;

import org.bukkit.entity.Player;

import com.citizens.npctypes.questers.Reward;
import com.citizens.npctypes.questers.quests.QuestManager;
import com.citizens.npctypes.questers.quests.QuestManager.RewardType;
import com.citizens.resources.npclib.HumanNPC;

public class QuestReward implements Reward {
	private final String reward;

	public QuestReward(String quest) {
		this.reward = quest;
	}

	public void grantQuest(Player player, HumanNPC npc) {
		QuestManager.assignQuest(npc, player, reward);
	}

	@Override
	public void grant(Player player) {
	}

	@Override
	public RewardType getType() {
		return RewardType.QUEST;
	}

	@Override
	public Object getReward() {
		return reward;
	}

	@Override
	public boolean isTake() {
		return false;
	}

	@Override
	public boolean canTake(Player player) {
		return true;
	}
}