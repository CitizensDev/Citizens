package net.citizensnpcs.api.event;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;

public abstract class NPCPlayerEvent extends NPCEvent {
    protected Player player;

    protected NPCPlayerEvent(HumanNPC npc, Player player) {
        super(npc);
        this.player = player;
    }

    /**
     * Get the player who right-clicked an NPC
     * 
     * @return player who right-clicked NPC
     */
    public Player getPlayer() {
        return this.player;
    }
}
