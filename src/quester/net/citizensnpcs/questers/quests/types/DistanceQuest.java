package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.quests.ObjectiveProgress;
import net.citizensnpcs.questers.quests.QuestObjective;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerMoveEvent;

public class DistanceQuest implements QuestObjective {

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent ev = (PlayerMoveEvent) event;
			int x = Math.abs(ev.getTo().getBlockX() - ev.getFrom().getBlockX());
			int y = Math.abs(ev.getTo().getBlockY() - ev.getFrom().getBlockY());
			int z = Math.abs(ev.getTo().getBlockZ() - ev.getFrom().getBlockZ());
			progress.incrementCompleted(x + y + z);

		}
		return progress.getAmount() >= progress.getObjective().getAmount();
	}

	@Override
	public Type[] getEventTypes() {
		return new Type[] { Type.PLAYER_MOVE };
	}

	@Override
	public boolean isCompleted(ObjectiveProgress progress) {
		return progress.getAmount() >= progress.getObjective().getAmount();
	}
}