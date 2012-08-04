package net.citizensnpcs.api.event;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class NPCRightClickEvent extends NPCPlayerEvent implements Cancellable {
    private boolean cancelled = false;

    public NPCRightClickEvent(HumanNPC npc, Player player) {
        super(npc, player);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Get the cancellation state of the event.
     * 
     * @return true if the event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Set the cancellation state of an event.
     * 
     * @param cancelled
     *            the cancellation state of the event
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }
}