package net.citizensnpcs.api.event;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class NPCCreateEvent extends NPCEvent {
    private final NPCCreateReason reason;
    private final Location loc;

    public NPCCreateEvent(HumanNPC npc, NPCCreateReason reason, Location loc) {
        super(npc);
        this.reason = reason;
        this.loc = loc;
    }

    /**
     * Get the reason why an NPC was created
     * 
     * @return reason for an NPC being created
     */
    public NPCCreateReason getReason() {
        return this.reason;
    }

    /**
     * Get the location where the NPC was created
     * 
     * @return location where NPC was created
     */
    public Location getLocation() {
        return this.loc;
    }

    public enum NPCCreateReason {
        /**
         * NPC naturally spawned into the world
         */
        SPAWN,
        /**
         * NPC was created via a command
         */
        COMMAND,
        /**
         * NPC was reloaded/respawned after being unloaded/despawned/killed
         */
        RESPAWN;
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