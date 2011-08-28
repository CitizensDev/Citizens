package net.citizensnpcs.api.event.npc;

import net.citizensnpcs.resources.npclib.HumanNPC;

public class NPCToggleTypeEvent extends NPCEvent {
	private static final long serialVersionUID = 1L;
	private String type;
	private boolean toggledOn;

	public NPCToggleTypeEvent(HumanNPC npc, String type, boolean toggledOn) {
		super("NPCToggleTypeEvent", npc);
		this.type = type;
		this.toggledOn = toggledOn;
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
}