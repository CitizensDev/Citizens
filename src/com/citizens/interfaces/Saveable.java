package com.citizens.interfaces;

import com.citizens.resources.npclib.HumanNPC;

public interface Saveable {
	/**
	 * Save the state of an NPC
	 * 
	 * @param npc
	 */
	public abstract void saveState(HumanNPC npc);

	/**
	 * Load the state of an NPC
	 * 
	 * @param npc
	 */
	public abstract void loadState(HumanNPC npc);

	/**
	 * Register an NPC
	 * 
	 * @param npc
	 */
	public abstract void register(HumanNPC npc);

	/**
	 * Set the state of an NPC
	 * 
	 * @param npc
	 * @param value
	 */
	public abstract void setEnabled(HumanNPC npc, boolean value);

	/**
	 * Get the state of an NPC
	 * 
	 * @param npc
	 * @return
	 */
	public abstract boolean getEnabled(HumanNPC npc);

	/**
	 * Copy an NPC
	 * 
	 * @param UID
	 * @param nextUID
	 */
	public abstract void copy(int UID, int nextUID);
}