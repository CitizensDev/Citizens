package com.citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.citizens.NPCTypes.Questers.Objectives.Objectives.ObjectiveCycler;
import com.citizens.NPCTypes.Questers.Quests.QuestIncrementer;
import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Utils.LocationUtils;

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