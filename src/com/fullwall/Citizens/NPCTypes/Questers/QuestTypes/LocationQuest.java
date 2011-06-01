package com.fullwall.Citizens.NPCTypes.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.fullwall.Citizens.NPCTypes.Questers.QuestProgress;
import com.fullwall.Citizens.Utils.LocationUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class LocationQuest extends QuestProgress {
	public LocationQuest(HumanNPC npc, Player player, String questName) {
		super(npc, player, questName);
	}

	@Override
	public void updateProgress(Event event) {
		// Possibility for a distance away parameter.
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent ev = (PlayerMoveEvent) event;
			if (LocationUtils.checkLocation(ev.getPlayer().getLocation(),
					getObjectiveLocation(), 0))
				this.lastLocation = ev.getPlayer().getLocation();
		}
	}

	@Override
	public boolean isCompleted() {
		return LocationUtils.checkLocation(getObjectiveLocation(),
				lastLocation, 0);
	}
}