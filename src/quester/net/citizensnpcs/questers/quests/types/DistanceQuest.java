package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerMoveEvent;

public class DistanceQuest implements QuestUpdater {
	private static final Type[] EVENTS = new Type[] { Type.PLAYER_MOVE };

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent ev = (PlayerMoveEvent) event;
			int x = Math.abs(ev.getTo().getBlockX() - ev.getFrom().getBlockX());
			int y = Math.abs(ev.getTo().getBlockY() - ev.getFrom().getBlockY());
			int z = Math.abs(ev.getTo().getBlockZ() - ev.getFrom().getBlockZ());
			progress.incrementCompleted(x + y + z);

		}
		return isCompleted(progress);
	}

	@Override
	public Type[] getEventTypes() {
		return EVENTS;
	}

	@Override
	public boolean isCompleted(ObjectiveProgress progress) {
		return progress.getAmount() >= progress.getObjective().getAmount();
	}

	@Override
	public String getStatus(ObjectiveProgress progress) {
		return QuestUtils.defaultAmountProgress(progress, StringUtils
				.formatter("block").wrap().plural(progress.getAmount())
				+ " walked");
	}
}