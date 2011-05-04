package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Quest type involving a player traveling to a set location
 */
public class LocationQuest implements Quest {
	private HumanNPC questGiver;
	private HumanNPC endNPC;
	private int distanceTraveled;

	public LocationQuest(HumanNPC questGiver, HumanNPC endNPC,
			int distanceTraveled) {
		this.questGiver = questGiver;
		this.endNPC = endNPC;
		this.distanceTraveled = distanceTraveled;
		distanceTraveled = questGiver.getLocation().getBlockX()
				- endNPC.getLocation().getBlockX();
	}

	/**
	 * 
	 * @return the NPC that a player must travel to during a LocationQuest
	 */
	public HumanNPC getEndNPC() {
		return endNPC;
	}

	/**
	 * 
	 * @param endNPC
	 *            the NPC that a player must travel to during a LocationQuest
	 */
	public void setEndNPC(HumanNPC endNPC) {
		this.endNPC = endNPC;
	}

	/**
	 * 
	 * @return the distance traveled by a player during a LocationQuest
	 */
	public int getDistanceTraveled() {
		return distanceTraveled;
	}

	/**
	 * Interface methods
	 */
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
	public HumanNPC getQuestGiver() {
		return questGiver;
	}

	@Override
	public void setQuestGiver(HumanNPC questGiver) {
		this.questGiver = questGiver;
	}
}