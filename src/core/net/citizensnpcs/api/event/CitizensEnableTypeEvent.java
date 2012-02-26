package net.citizensnpcs.api.event;

import net.citizensnpcs.npctypes.CitizensNPCType;

import org.bukkit.event.HandlerList;

public class CitizensEnableTypeEvent extends CitizensEvent {
    private static final long serialVersionUID = 1L;
    private final CitizensNPCType type;

    public CitizensEnableTypeEvent(CitizensNPCType type) {
        this.type = type;
    }

    public CitizensNPCType getEnabledType() {
        return this.type;
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
