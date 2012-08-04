package net.citizensnpcs.api.event;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class NPCCreateEvent extends NPCEvent {
    private final Location loc;
    private final NPCCreateReason reason;

    public NPCCreateEvent(HumanNPC npc, NPCCreateReason reason, Location loc) {
        super(npc);
        this.reason = reason;
        this.loc = loc;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Get the location where the NPC was created
     * 
     * @return location where NPC was created
     */
    public Location getLocation() {
        return this.loc;
    }

    /**
     * Get the reason why an NPC was created
     * 
     * @return reason for an NPC being created
     */
    public NPCCreateReason getReason() {
        return this.reason;
    }

    public enum NPCCreateReason {
        /**
         * NPC was created via a command
         */
        COMMAND,
        /**
         * NPC was reloaded/respawned after being unloaded/despawned/killed
         */
        RESPAWN,
        /**
         * NPC naturally spawned into the world
         */
        SPAWN;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }
}