package net.citizensnpcs.properties;

import java.util.Collection;
import java.util.List;

import net.citizensnpcs.resources.npclib.HumanNPC;

/**
 * This interface is used for registering properties in the various Citizens
 * config files and npc-profiles.yml.
 * 
 */
public interface Properties {
	/**
	 * Get a list of configuration nodes for an NPC type
	 * 
	 * @see net.citizensnpcs.properties.Node Node for info on creating nodes
	 * 
	 * @return list of configuration nodes
	 */
	public abstract List<Node> getNodes();

	public abstract Collection<String> getNodesForCopy();

	/**
	 * Get whether an NPC is of a type
	 * 
	 * @param npc
	 *            NPC to get the state of
	 * @return true if the given NPC is the type
	 */
	public abstract boolean isEnabled(HumanNPC npc);

	/**
	 * Load data for an NPC type for the given NPC
	 * 
	 * @param npc
	 *            NPC to load the state of
	 */
	public abstract void loadState(HumanNPC npc);

	/**
	 * Load data for an NPC type for the given NPC
	 * 
	 * @param npc
	 *            NPC to save the state of
	 */
	public abstract void saveState(HumanNPC npc);

	/**
	 * Set whether an NPC is of a type
	 * 
	 * @param npc
	 *            NPC to set the state of
	 * @param value
	 *            Whether or not an NPC is enabled as a type
	 */
	public abstract void setEnabled(HumanNPC npc, boolean value);
}