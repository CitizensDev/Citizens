package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.Questers.PlayerProfile;
import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class DistanceQuest implements Quest {

	@Override
	public QuestType getType() {
		// TODO Auto-generated method stub
		return QuestType.MOVE_DISTANCE;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HumanNPC getQuester() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCompletedText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateProgress(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public PlayerProfile getPlayerProfile() {
		// TODO Auto-generated method stub
		return null;
	}

}
