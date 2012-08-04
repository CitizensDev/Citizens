package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.quests.Objective;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class CollectQuest implements QuestUpdater {
    private boolean checkData(Objective objective, ItemStack stack) {
        return !objective.hasParameter("data") || objective.getParameter("data").getInt() == stack.getDurability();
    }

    @Override
    public Class<? extends Event>[] getEventTypes() {
        return EVENTS;
    }

    @Override
    public String getStatus(ObjectiveProgress progress) {
        return QuestUtils.defaultAmountProgress(progress, StringUtils.formatter(progress.getObjective().getMaterial())
                .wrap().plural(progress.getAmount())
                + " collected");
    }

    @Override
    public boolean update(Event event, ObjectiveProgress progress) {
        if (event instanceof PlayerPickupItemEvent) {
            PlayerPickupItemEvent ev = (PlayerPickupItemEvent) event;
            ItemStack stack = ev.getItem().getItemStack();
            if (stack.getType() == progress.getObjective().getMaterial() && checkData(progress.getObjective(), stack))
                progress.addAmount(stack.getAmount());
        }
        return progress.getAmount() >= progress.getObjective().getAmount();
    }

    private static final Class<? extends Event>[] EVENTS = new Class[] { PlayerPickupItemEvent.class };
}