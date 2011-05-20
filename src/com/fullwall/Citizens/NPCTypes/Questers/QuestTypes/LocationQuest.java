package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.NPCTypes.Questers.PlayerProfile;
import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestManager.QuestType;
import com.fullwall.Citizens.NPCTypes.Questers.Reward;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class LocationQuest implements Quest {

	@Override
	public QuestType getType() {
		// TODO Auto-generated method stub
		return QuestType.MOVE_LOCATION;
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

	@Override
	public List<Reward> getRewards() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addReward(Reward reward) {
		// TODO Auto-generated method stub
		
	}
}
