package net.citizensnpcs.api.events;

import net.citizensnpcs.resources.npclib.HumanNPC;

public class NPCRemoveEvent extends NPCEvent {
	private static final long serialVersionUID = 1L;
	
	public NPCRemoveEvent(HumanNPC npc) {
		super("NPCRemoveEvent", npc);
	}
}