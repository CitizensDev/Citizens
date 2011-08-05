package net.citizensnpcs.questers.questtypes;

import net.citizensnpcs.questers.objectives.Objectives.ObjectiveCycler;
import net.citizensnpcs.questers.quests.QuestIncrementer;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.LocationUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerMoveEvent;

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
			if (LocationUtils.withinRange(ev.getPlayer().getLocation(),
					this.objective.getLocation(), 0))
				this.getProgress()
						.setLastLocation(ev.getPlayer().getLocation());
		}
	}

	@Override
	public boolean isCompleted() {
		return LocationUtils.withinRange(this.objective.getLocation(), this
				.getProgress().getLastLocation(), 0);
	}

	@Override
	public Type[] getEventTypes() {
		return new Type[] { Type.PLAYER_MOVE };
	}
}