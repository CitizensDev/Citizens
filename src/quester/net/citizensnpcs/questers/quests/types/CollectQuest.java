package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.quests.ObjectiveProgress;
import net.citizensnpcs.questers.quests.QuestObjective;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class CollectQuest implements QuestObjective {

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
		return progress.getAmount() >= progress.getObjective().getAmount();
	}

	@Override
	public Type[] getEventTypes() {
		return new Type[] { Type.PLAYER_PICKUP_ITEM };
	}
}