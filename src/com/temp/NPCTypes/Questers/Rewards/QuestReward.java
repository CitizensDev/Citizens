package com.temp.NPCTypes.Questers.Rewards;

import org.bukkit.entity.Player;

import com.temp.NPCTypes.Questers.Reward;
import com.temp.NPCTypes.Questers.Quests.QuestManager;
import com.temp.NPCTypes.Questers.Quests.QuestManager.RewardType;
import com.temp.resources.redecouverte.NPClib.HumanNPC;

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