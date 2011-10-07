package net.citizensnpcs.api;

import net.citizensnpcs.npcdata.NPCDataManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCList;
import net.citizensnpcs.resources.npclib.NPCManager;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CitizensManager {

	/**
	 * Gets an NPC from a UID.
	 * 
	 * @param UID
	 *            UID of an NPC
	 * @return NPC with the given ID, null if an NPC wasn't found
	 */
	public static HumanNPC getNPC(int UID) {
		return NPCManager.get(UID);
	}

	/**
	 * Gets an NPC from an entity.
	 * 
	 * @param entity
	 *            Bukkit Entity
	 * @return NPC, null if an NPC wasn't found
	 */
	public static HumanNPC get(Entity entity) {
		return NPCManager.get(entity);
	}

	/**
	 * Checks if a given entity is an npc.
	 * 
	 * @param entity
	 *            Bukkit Entity
	 * @return true if the entity is an NPC
	 */
	public static boolean isNPC(Entity entity) {
		return NPCManager.isNPC(entity);
	}

	/**
	 * Gets the list of NPCs.
	 * 
	 * @return list of NPCs on a server
	 */
	public static NPCList getList() {
		return NPCManager.getList();
	}

	/**
	 * Get the NPC that a given player has selected
	 * 
	 * @param player
	 *            to check if an NPC is selected
	 * 
	 * @return selected NPC, null if no NPC is selected
	 */
	public static int getSelected(Player player) {
		return NPCDataManager.getSelected(player);
	}

	/**
	 * Checks if a player has an npc selected.
	 * 
	 * @param player
	 *            Player to check
	 * @return true if the player has an NPC selected
	 */
	public static boolean validateSelected(Player player) {
		return NPCManager.hasSelected(player);
	}

	/**
	 * Checks if the player has selected the given npc.
	 * 
	 * @param player
	 *            Player to check
	 * @param UID
	 *            UID of the NPC to check
	 * @return true if the player has the NPC with the given UID selected
	 */
	public static boolean validateSelected(Player player, int UID) {
		return NPCManager.hasSelected(player, UID);
	}
}