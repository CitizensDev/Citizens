package com.fullwall.Citizens.Interfaces;

import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public abstract class Toggleable {
	protected final HumanNPC npc;

	public Toggleable(HumanNPC npc) {
		this.npc = npc;
	}

	/**
	 * Get an NPC's name
	 * 
	 * @return
	 */
	public String getName() {
		return this.npc.getStrippedName();
	}

	/**
	 * Get an NPC's type
	 * 
	 * @return
	 */
	public abstract String getType();

	/**
	 * Save an NPC's state
	 * 
	 * @return
	 */
	public void saveState() {
		PropertyManager.get(getType()).saveState(npc);
	}

	/**
	 * Register an NPC
	 * 
	 * @return
	 */
	public void register() {
		PropertyManager.get(getType()).register(npc);
	}
}