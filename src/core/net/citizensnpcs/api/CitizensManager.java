package net.citizensnpcs.api;

import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.npcs.NPCManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCList;

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
	 * Helper method used to register events per-type
	 * 
	 * @param eventType
	 *            Bukkit Event.Type
	 * @param listener
	 *            Bukkit Listener interface
	 * @param priority
	 *            Bukkit Event.Priority
	 */
	public static void registerEvent(Type eventType, Listener listener,
			Priority priority) {
		Citizens.plugin.getServer().getPluginManager()
				.registerEvent(eventType, listener, priority, Citizens.plugin);
	}

	/**
	 * Helper method used to register events per-type with default "Normal"
	 * priority
	 * 
	 * @param eventType
	 *            Bukkit Event.Type
	 * @param listener
	 *            Bukkit Listener interface
	 */
	public static void registerEvent(Type eventType, Listener listener) {
		registerEvent(eventType, listener, Priority.Normal);
	}

	/**
	 * Adds a permission node to a list of nodes to register for superperms
	 * 
	 * @param perm
	 *            Permission node to register (use format '(typeName).(perm)',
	 *            Citizens adds "citizens." to the beginning)
	 */
	public static void addPermission(String perm) {
		PermissionManager.addPermission(perm);
	}
}