package com.citizens.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;

import com.citizens.NPCTypes.Questers.Reward;
import com.citizens.NPCTypes.Questers.Quests.QuestManager;
import com.citizens.NPCTypes.Questers.Quests.QuestManager.RewardType;
import com.citizens.Resources.NPClib.HumanNPC;

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
}