package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.quests.ObjectiveProgress;
import net.citizensnpcs.questers.quests.QuestUpdater;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityDeathEvent;

public class CombatQuest implements QuestUpdater {
	private static final Type[] EVENTS = new Type[] { Type.ENTITY_DEATH };

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof EntityDeathEvent) {
			EntityDeathEvent ev = (EntityDeathEvent) event;
			if (ev.getEntity() instanceof Player) {
				Player player = (Player) ev.getEntity();
				if (progress.getObjective().getString()
						.contains(player.getName().toLowerCase())) {
					progress.incrementCompleted(1);
				}
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
		return QuestUtils.defaultAmountProgress(progress, "players defeated");
	}
}