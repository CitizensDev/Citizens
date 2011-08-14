package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.quests.ObjectiveProgress;
import net.citizensnpcs.questers.quests.QuestObjective;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class CollectQuest implements QuestObjective {
	private static final Type[] EVENTS = new Type[] { Type.PLAYER_PICKUP_ITEM };

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof PlayerPickupItemEvent) {
			PlayerPickupItemEvent ev = (PlayerPickupItemEvent) event;
			if (ev.getItem().getItemStack().getType() == progress
					.getObjective().getMaterial()) {
				progress.incrementCompleted(ev.getItem().getItemStack()
						.getAmount());
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
}