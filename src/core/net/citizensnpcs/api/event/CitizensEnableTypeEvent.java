package net.citizensnpcs.api.event;

import net.citizensnpcs.npctypes.CitizensNPCType;

import org.bukkit.event.HandlerList;

public class CitizensEnableTypeEvent extends CitizensEvent {
    private final CitizensNPCType type;

    public CitizensEnableTypeEvent(CitizensNPCType type) {
        this.type = type;
    }

    public CitizensNPCType getEnabledType() {
        return this.type;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
