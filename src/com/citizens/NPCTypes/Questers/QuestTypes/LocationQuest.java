package com.citizens.npctypes.questers.questtypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.citizens.npctypes.questers.objectives.Objectives.ObjectiveCycler;
import com.citizens.npctypes.questers.quests.QuestIncrementer;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.LocationUtils;

public class LocationQuest extends QuestIncrementer {
	public LocationQuest(HumanNPC npc, Player player, String questName,
			ObjectiveCycler objectives) {
		super(npc, player, questName, objectives);
	}

	@Override
	public void updateProgress(Event event) {
		// Possibility for a distance away parameter.
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent ev = (PlayerMoveEvent) event;
			if (LocationUtils.checkLocation(ev.getPlayer().getLocation(),
					this.objective.getLocation(), 0))
				this.getProgress()
						.setLastLocation(ev.getPlayer().getLocation());
		}
	}

	@Override
	public boolean isCompleted() {
		return LocationUtils.checkLocation(this.objective.getLocation(), this
				.getProgress().getLastLocation(), 0);
	}
}