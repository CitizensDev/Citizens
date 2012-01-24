package net.citizensnpcs.api.event;

import org.bukkit.event.HandlerList;

public class CitizensDisableEvent extends CitizensEvent {
    private static final long serialVersionUID = 1L;

    public CitizensDisableEvent() {
        super("CitizensDisableEvent");
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}