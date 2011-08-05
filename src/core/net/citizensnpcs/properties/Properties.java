package net.citizensnpcs.properties;

import java.util.List;

import net.citizensnpcs.resources.npclib.HumanNPC;

public interface Properties {
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
	 * Set the state of an NPC as a given type
	 * 
	 * @param npc
	 * @param value
	 */
	public abstract void setEnabled(HumanNPC npc, boolean value);

	/**
	 * Get the state of an NPC
	 * 
	 * @param npc
	 * @return true if the given NPC is the type
	 */
	public abstract boolean getEnabled(HumanNPC npc);

	/**
	 * Copy an NPC
	 * 
	 * @param UID
	 * @param nextUID
	 */
	public abstract void copy(int UID, int nextUID);

	/**
	 * Get a list of configuration nodes for an NPC type
	 * 
	 * @return list of configuration nodes
	 */
	public abstract List<Node> getNodes();
}