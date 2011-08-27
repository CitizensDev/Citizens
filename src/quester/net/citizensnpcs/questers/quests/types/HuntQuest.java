package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.quests.ObjectiveProgress;
import net.citizensnpcs.questers.quests.QuestUpdater;
import net.citizensnpcs.utils.EntityUtils;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HuntQuest implements QuestUpdater {
	private static final Type[] EVENTS = new Type[] { Type.ENTITY_DAMAGE };

	@Override
	public boolean update(Event event, ObjectiveProgress progress) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
			if (!(ev.getEntity() instanceof LivingEntity))
				return false;
			if (((LivingEntity) ev.getEntity()).getHealth() - ev.getDamage() <= 0) {
				LivingEntity entity = (LivingEntity) ev.getEntity();
				String search = progress.getObjective().getString();
				boolean found = search.contains(EntityUtils
						.getMonsterName(entity)) || search.contains("*"), reversed = !search
						.isEmpty() && search.charAt(0) == '-';
				if ((reversed && !found) || (!reversed && found)) {
					Messaging.log("death successful");
					progress.incrementCompleted(1);
				}
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