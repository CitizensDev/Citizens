package net.citizensnpcs.questers.api.events;

import net.citizensnpcs.questers.quests.Quest;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuestIncrementEvent extends QuestEvent implements Cancellable {
    private boolean cancelled;
    private final Event incrementedEvent;

    public QuestIncrementEvent(Quest quest, Player player, Event event) {
        super(quest, player);
        this.incrementedEvent = event;
    }

    /**
     * Returns the event that is updating quest progress.
     */
    public Event getEvent() {
        return incrementedEvent;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }
}