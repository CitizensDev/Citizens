package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Questers.Goal;
import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.Reward;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class LocationQuest implements Quest {
	private HumanNPC questGiver;
	private HumanNPC endNPC;
	private Location startLocation;
	private Location endLocation;
	private Goal goal;
	private Reward reward;

	public LocationQuest(HumanNPC questGiver, HumanNPC endNPC, Goal goal,
			Reward reward) {
		this.questGiver = questGiver;
		this.endNPC = endNPC;
		this.goal = goal;
		this.reward = reward;
	}

	public HumanNPC getEndNPC() {
		return endNPC;
	}

	public void setEndNPC(HumanNPC endNPC) {
		this.endNPC = endNPC;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	public Location getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Location endLocation) {
		this.endLocation = endLocation;
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
	public Player getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuestType getType() {
		return QuestType.LOCATION;
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
	public HumanNPC getQuestGiver() {
		return questGiver;
	}

	@Override
	public void setQuestGiver(HumanNPC questGiver) {
		this.questGiver = questGiver;
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
		// TODO Auto-generated method stub
		
	}
}