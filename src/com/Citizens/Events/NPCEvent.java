package com.citizens.Events;

import org.bukkit.event.Event;

import com.citizens.resources.redecouverte.NPClib.HumanNPC;

public class NPCEvent extends Event {
	private static final long serialVersionUID = 1L;
	private HumanNPC npc;

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