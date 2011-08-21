package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.quests.ObjectiveProgress;
import net.citizensnpcs.questers.quests.QuestUpdater;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Monster;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityDeathEvent;

public class HuntQuest implements QuestUpdater {
	private static final Type[] EVENTS = new Type[] { Type.ENTITY_DEATH };

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof EntityDeathEvent) {
			EntityDeathEvent ev = (EntityDeathEvent) event;
			if (ev.getEntity() instanceof Monster
					|| ev.getEntity() instanceof Creature) {
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

	@Override
	public String getStatus(ObjectiveProgress progress) {
		return QuestUtils.defaultAmountProgress(progress, "monsters killed");
	}
}