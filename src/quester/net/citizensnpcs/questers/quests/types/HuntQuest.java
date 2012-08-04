package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;

public class HuntQuest implements QuestUpdater {
    @Override
    public Class<? extends Event>[] getEventTypes() {
        return EVENTS;
    }

    @Override
    public String getStatus(ObjectiveProgress progress) {
        return QuestUtils.defaultAmountProgress(progress, "monsters killed");
    }

    @Override
    public boolean update(Event event, ObjectiveProgress progress) {
        if (event instanceof EntityDeathEvent) {
            EntityDeathEvent ev = (EntityDeathEvent) event;
            if (ev.getEntity() instanceof Player)
                return progress.getAmount() >= progress.getObjective().getAmount();
            LivingEntity entity = ev.getEntity();
            String search = progress.getObjective().getString().toLowerCase();
            boolean found = search.contains(entity.getType().getName().toLowerCase()) || search.contains("*"), reversed = !search
                    .isEmpty() && search.charAt(0) == '-';
            if (reversed ^ found) {
                progress.addAmount(1);
            }
        }
        return progress.getAmount() >= progress.getObjective().getAmount();
    }

    private static final Class<? extends Event>[] EVENTS = new Class[] { EntityDeathEvent.class };
}