package net.citizensnpcs.api.event;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.event.HandlerList;

public class NPCRemoveEvent extends NPCEvent {
    private final NPCRemoveReason reason;

    public NPCRemoveEvent(HumanNPC npc, NPCRemoveReason reason) {
        super(npc);
        this.reason = reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Get the reason why an NPC despawn
     * 
     * @return reason Reason why NPC despawned
     */
    public NPCRemoveReason getReason() {
        return this.reason;
    }

    public enum NPCRemoveReason {
        /**
         * NPC was removed by a command
         */
        COMMAND,
        /**
         * NPC was killed
         */
        DEATH,
        /**
         * NPC was removed by other means
         */
        OTHER,
        /**
         * NPC was removed, possibly to be respawned later
         */
        UNLOAD;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }
}