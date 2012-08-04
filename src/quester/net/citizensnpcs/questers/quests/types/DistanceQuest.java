package net.citizensnpcs.questers.quests.types;

import net.citizensnpcs.questers.QuestUtils;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

public class DistanceQuest implements QuestUpdater {
    @Override
    public Class<? extends Event>[] getEventTypes() {
        return EVENTS;
    }

    @Override
    public String getStatus(ObjectiveProgress progress) {
        return QuestUtils.defaultAmountProgress(progress,
                StringUtils.formatter("block").wrap().plural(progress.getAmount()) + " walked");
    }

    @Override
    public boolean update(Event event, ObjectiveProgress progress) {
        if (event instanceof PlayerMoveEvent) {
            PlayerMoveEvent ev = (PlayerMoveEvent) event;
            Location from = ev.getFrom(), to = ev.getTo();
            if (!(from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to
                    .getBlockZ())) {
                int x = Math.abs(to.getBlockX() - from.getBlockX());
                int y = Math.abs(to.getBlockY() - from.getBlockY());
                int z = Math.abs(to.getBlockZ() - from.getBlockZ());
                progress.addAmount(x + y + z);
            }
        }
        return progress.getAmount() >= progress.getObjective().getAmount();
    }

    private static final Class<? extends Event>[] EVENTS = new Class[] { PlayerMoveEvent.class };
}