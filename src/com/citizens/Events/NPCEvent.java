package com.citizens.events;

import org.bukkit.event.Event;

import com.citizens.resources.npclib.HumanNPC;

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