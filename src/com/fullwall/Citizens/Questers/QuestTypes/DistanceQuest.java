package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Quest type involving traveling a certain distance
 */
public class DistanceQuest implements Quest {
	private HumanNPC questGiver;
	private int startLocation;
	private int endLocation;
	private int distanceTraveled;

	public DistanceQuest(HumanNPC questGiver, int distanceTraveled,
			int endLocation) {
		this.questGiver = questGiver;
		this.startLocation = questGiver.getLocation().getBlockX();
		this.endLocation = endLocation;
		this.distanceTraveled = distanceTraveled;
	}

	/**
	 * 
	 * @return the location where a player begins his journey during a
	 *         DistanceQuest
	 */
	public int getStartLocation() {
		return startLocation;
	}

	/**
	 * 
	 * @return the location where a player ends his journey during a
	 *         DistanceQuest
	 */
	public int getEndLocation() {
		return endLocation;
	}

	/**
	 * 
	 * @return the distance traveled by a player during a DistanceQuest
	 */
	public int getDistanceTraveled() {
		return distanceTraveled;
	}

	/**
	 * 
	 * @param distanceTraveled
	 *            the distance traveled by a player during a DistanceQuest
	 */
	public void setDistanceTraveled(int distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}

	/**
	 * Interface methods
	 */
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
	public String getProgress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}
}