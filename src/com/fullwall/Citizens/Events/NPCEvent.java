package com.fullwall.Citizens.Events;

import org.bukkit.event.Event;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class NPCEvent extends Event {
	private static final long serialVersionUID = 1L;
	private HumanNPC npc;

	protected NPCEvent(String name, HumanNPC npc) {
		super(name);
		this.npc = npc;
	}

	public HumanNPC getNPC() {
		return npc;
	}

	public void setNPC(HumanNPC npc) {
		this.npc = npc;
	}
}