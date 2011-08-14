package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.quests.ObjectiveProgress;
import net.citizensnpcs.questers.quests.QuestObjective;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockBreakEvent;

public class DestroyQuest implements QuestObjective {
	private static final Type[] EVENTS = new Type[] { Type.BLOCK_BREAK };

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof BlockBreakEvent) {
			BlockBreakEvent ev = (BlockBreakEvent) event;
			if (ev.getBlock().getType() == progress.getObjective()
					.getMaterial()) {
				progress.incrementCompleted(1);
			}
		}
		return progress.getAmount() >= progress.getObjective().getAmount();
	}

	@Override
	public Type[] getEventTypes() {
		return EVENTS;
	}

	@Override
	public boolean isCompleted(ObjectiveProgress progress) {
		return progress.getAmount() >= progress.getObjective().getAmount();
	}
}