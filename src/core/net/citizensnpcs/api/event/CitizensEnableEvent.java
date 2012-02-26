package net.citizensnpcs.api.event;

import org.bukkit.event.HandlerList;

public class CitizensEnableEvent extends CitizensEvent {
    private static final long serialVersionUID = 1L;

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}