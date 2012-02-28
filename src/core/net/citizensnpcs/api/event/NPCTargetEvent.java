package net.citizensnpcs.api.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTargetEvent;

public class NPCTargetEvent extends EntityTargetEvent {
    public NPCTargetEvent(Entity entity, Entity target) {
        super(entity, target, TargetReason.CUSTOM);
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