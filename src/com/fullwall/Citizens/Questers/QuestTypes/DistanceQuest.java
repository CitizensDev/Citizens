package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Questers.Goal;
import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.Reward;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class DistanceQuest implements Quest {
	private HumanNPC questGiver;
	private int distanceTraveled;
	private Goal goal;
	private Reward reward;

	public DistanceQuest(HumanNPC questGiver, int distanceTraveled, Goal goal,
			Reward reward) {
		this.questGiver = questGiver;
		this.distanceTraveled = distanceTraveled;
		this.goal = goal;
		this.reward = reward;
	}

	public int getDistanceTraveled() {
		return distanceTraveled;
	}

	public void setDistanceTraveled(int distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}

	@Override
	public HumanNPC getQuestGiver() {
		return questGiver;
	}

	@Override
	public void setQuestGiver(HumanNPC questGiver) {
		this.questGiver = questGiver;
	}

	@Override
	public Player getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuestType getType() {
		return QuestType.DISTANCE;
	}

	@Override
	public Goal getGoal() {
		return goal;
	}

	@Override
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	@Override
	public String getProgress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Reward getReward() {
		return reward;
	}

	@Override
	public void setReward(Reward reward) {
		this.reward = reward;
	}

	@Override
	public void rewardPlayer(Player player, Reward reward) {
		// TODO put stuff here
	}
}