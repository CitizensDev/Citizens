package net.citizensnpcs.api.event;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.event.HandlerList;

public class NPCToggleTypeEvent extends NPCEvent {
    private final boolean toggledOn;
    private final String type;

    public NPCToggleTypeEvent(HumanNPC npc, String type, boolean toggledOn) {
        super(npc);
        this.type = type;
        this.toggledOn = toggledOn;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Get the type that an NPC was toggled
     * 
     * @return type that was toggled for an NPC
     */
    public String getToggledType() {
        return this.type;
    }

    /**
     * Get whether the type was toggled on
     * 
     * @return true if the type was toggled on
     */
    public boolean isToggledOn() {
        return this.toggledOn;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }
}