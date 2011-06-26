package com.citizens.Interfaces;

import com.citizens.Properties.PropertyManager.PropertyType;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;

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
	 * Get an NPC's PropertyType
	 * 
	 * @return
	 */
	public abstract PropertyType type();

	/**
	 * Copy an NPC
	 * 
	 * @param UID
	 * @param nextUID
	 */
	public abstract void copy(int UID, int nextUID);
}