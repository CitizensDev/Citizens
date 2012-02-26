package net.citizensnpcs.api.event;

import net.citizensnpcs.resources.npclib.HumanNPC;

public abstract class NPCEvent extends CitizensEvent {
    private static final long serialVersionUID = 1L;
    private final HumanNPC npc;

    protected NPCEvent(HumanNPC npc) {
        this.npc = npc;
    }

    /**
     * Get the npc involved in the event.
     * 
     * @return the npc involved in the event
     */
    public HumanNPC getNPC() {
        return npc;
    }
}