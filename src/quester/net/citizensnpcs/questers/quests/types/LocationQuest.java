package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.quests.ObjectiveProgress;
import net.citizensnpcs.questers.quests.QuestObjective;
import net.citizensnpcs.utils.LocationUtils;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerMoveEvent;

public class LocationQuest implements QuestObjective {

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		// Possibility for a distance away parameter.
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent ev = (PlayerMoveEvent) event;
			if (LocationUtils.withinRange(ev.getPlayer().getLocation(),
					progress.getObjective().getLocation(), 0))
				progress.setLastLocation(ev.getPlayer().getLocation());
		}
		return LocationUtils.withinRange(progress.getObjective().getLocation(),
				progress.getLastLocation(), 0);
	}

	@Override
	public Type[] getEventTypes() {
		return new Type[] { Type.PLAYER_MOVE };
	}
}