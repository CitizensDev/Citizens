package net.citizensnpcs.api.events;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.event.Event;

public class NPCEvent extends Event {
	private static final long serialVersionUID = 1L;
	private final HumanNPC npc;

	public NPCEvent(String name, HumanNPC npc) {
		super(name);
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