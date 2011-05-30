package com.fullwall.Citizens.Interfaces;

import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public abstract class Saveable {

	/**
	 * Save files to disk
	 * 
	 * @return
	 */
	public abstract void saveFiles();

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
	 * Remove an NPC from the disk files
	 * 
	 * @param npc
	 */
	public abstract void removeFromFiles(HumanNPC npc);

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
	 * Get a file by it's name
	 * 
	 * @param name
	 * @return
	 */
	public Storage getFile(String name) {
		return PropertyManager.getHandler(this.getClass(), name, this);
	}

	/**
	 * Check if an NPC exists
	 * 
	 * @param npc
	 * @return
	 */
	public abstract boolean exists(HumanNPC npc);

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