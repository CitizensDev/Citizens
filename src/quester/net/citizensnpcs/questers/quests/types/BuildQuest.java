package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildQuest implements QuestUpdater {
	private static final Type[] EVENTS = new Type[] { Type.BLOCK_PLACE };

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof BlockPlaceEvent) {
			if (((BlockPlaceEvent) event).getBlockPlaced().getType() == progress
					.getObjective().getMaterial()) {
				progress.incrementCompleted(1);
			}
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
		return QuestUtils.defaultAmountProgress(progress,
				StringUtils.formatter(progress.getObjective().getMaterial())
						.plural(progress.getAmount()) + " built");
	}
}