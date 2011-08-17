package net.citizensnpcs.npcs;

import java.util.ArrayList;
import java.util.Map;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.events.NPCSpawnEvent;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCList;
import net.citizensnpcs.resources.npclib.NPCSpawner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.google.common.collect.MapMaker;

public class NPCManager {
	public static final Map<Integer, String> GlobalUIDs = new MapMaker()
			.makeMap();
	private static NPCList list = new NPCList();

	/**
	 * Spawns a new npc and registers it.
	 * 
	 * @param UID
	 * @param owner
	 */
	public static void register(int UID, String owner) {
		Location loc = PropertyManager.getBasic().getLocation(UID);

		ChatColor colour = PropertyManager.getBasic().getColour(UID);
		String name = PropertyManager.getBasic().getName(UID);
		name = ChatColor.stripColor(name);
		if (SettingsManager.getBoolean("ConvertSlashes")) {
			name = name.replace(Citizens.separatorChar, " ");
		}
		String npcName = name;
		if (colour != ChatColor.WHITE) {
			npcName = colour + name;
		}
		HumanNPC npc = NPCSpawner.spawnNPC(UID, npcName, loc.getWorld(),
				loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0F);
		npc.teleport(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0F);

		NPCSpawnEvent event = new NPCSpawnEvent(npc);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			NPCSpawner.despawnNPC(npc);
			return;
		}

		ArrayList<Integer> items = PropertyManager.getBasic().getItems(UID);

		npc.setNPCData(new NPCData(npcName, UID, loc, colour, items,
				NPCDataManager.NPCTexts.get(UID), PropertyManager.getBasic()
						.isLookWhenClose(UID), PropertyManager.getBasic()
						.isTalkWhenClose(UID), owner));
		PropertyManager.getBasic().saveOwner(UID, owner);
		PropertyManager.load(npc);

		registerUID(UID, npcName);
		list.put(UID, npc);
		PropertyManager.save(npc);

		npc.getPlayer().setSleepingIgnored(true); // Fix beds.
	}

	/**
	 * Registers a new npc.
	 * 
	 * @param name
	 * @param loc
	 * @param owner
	 * @return
	 */
	public static int register(String name, Location loc, String owner) {
		int UID = PropertyManager.getBasic().getNewNpcID();
		PropertyManager.getBasic().saveLocation(loc, UID);
		PropertyManager.getBasic().saveLookWhenClose(UID,
				SettingsManager.getBoolean("DefaultLookAt"));
		PropertyManager.getBasic().saveTalkWhenClose(UID,
				SettingsManager.getBoolean("DefaultTalkClose"));
		PropertyManager.getBasic().saveName(UID, name);
		register(UID, owner);
		return UID;
	}

	/**
	 * Gets an npc from a UID.
	 * 
	 * @param UID
	 * @return
	 */
	public static HumanNPC get(int UID) {
		return list.get(UID);
	}

	/**
	 * Gets an npc from an entity.
	 * 
	 * @param entity
	 * @return
	 */
	public static HumanNPC get(Entity entity) {
		return list.getNPC(entity);
	}

	/**
	 * Gets the list of npcs.
	 * 
	 * @return
	 */
	public static NPCList getList() {
		return list;
	}

	/**
	 * Rotates an npc.
	 * 
	 * @param npc
	 * @param player
	 */
	public static void facePlayer(HumanNPC npc, Player player) {
		Location loc = npc.getLocation(), pl = player.getLocation();
		double xDiff = pl.getX() - loc.getX();
		double yDiff = pl.getY() - loc.getY();
		double zDiff = pl.getZ() - loc.getZ();
		double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
		double yaw = (Math.acos(xDiff / DistanceXZ) * 180 / Math.PI);
		double pitch = (Math.acos(yDiff / DistanceY) * 180 / Math.PI) - 90;
		if (zDiff < 0.0) {
			yaw = yaw + (Math.abs(180 - yaw) * 2);
		}
		npc.teleport(loc.getX(), loc.getY(), loc.getZ(), (float) yaw - 90,
				(float) pitch);
		if (npc.getOwner().equals(player.getName())) {
			loc = npc.getNPCData().getLocation();
			loc.setPitch(npc.getLocation().getPitch());
			loc.setYaw(npc.getLocation().getYaw());
			npc.getNPCData().setLocation(loc);
		}
	}

	/**
	 * Despawns an npc.
	 * 
	 * @param UID
	 */
	public static void despawn(int UID) {
		GlobalUIDs.remove(UID);
		NPCSpawner.despawnNPC(list.remove(UID));
	}

	/**
	 * Despawns all npcs.
	 */
	public static void despawnAll() {
		for (Integer i : GlobalUIDs.keySet()) {
			despawn(i);
		}
	}

	/**
	 * Removes an npc.
	 * 
	 * @param UID
	 */
	public static void remove(int UID) {
		PropertyManager.remove(get(UID));
		NPCSpawner.despawnNPC(list.remove(UID));
		GlobalUIDs.remove(UID);
	}

	public static void removeAll() {
		for (Integer i : GlobalUIDs.keySet()) {
			remove(i);
		}
	}

	/**
	 * Removes an npc, but not from the properties.
	 * 
	 * @param UID
	 */
	public static void removeForRespawn(int UID) {
		PropertyManager.save(list.get(UID));
		despawn(UID);
	}

	/**
	 * Gets the global list of UIDs.
	 * 
	 * @return
	 */
	public Map<Integer, String> getUIDs() {
		return GlobalUIDs;
	}

	/**
	 * Registers a UID in the global list.
	 * 
	 * @param UID
	 * @param name
	 */
	private static void registerUID(int UID, String name) {
		GlobalUIDs.put(UID, name);
	}

	/**
	 * Checks if a given entity is an npc.
	 * 
	 * @param entity
	 * @return
	 */
	public static boolean isNPC(Entity entity) {
		return list.getNPC(entity) != null;
	}

	/**
	 * Checks if a player has an npc selected.
	 * 
	 * @param p
	 * @return
	 */
	public static boolean validateSelected(Player p) {
		return NPCDataManager.selectedNPCs.get(p.getName()) != null
				&& !NPCDataManager.selectedNPCs.get(p.getName()).toString()
						.isEmpty();
	}

	/**
	 * Checks if the player has selected the given npc.
	 * 
	 * @param p
	 * @param npc
	 * @return
	 */
	public static boolean validateSelected(Player p, int UID) {
		return validateSelected(p)
				&& NPCDataManager.selectedNPCs.get(p.getName()) == UID;
	}

	/**
	 * Checks if a player owns a given npc.
	 * 
	 * @param player
	 * @param UID
	 * @return
	 */
	public static boolean validateOwnership(Player player, int UID,
			boolean checkAdmin) {
		return (checkAdmin && PermissionManager.generic(player,
				"citizens.admin"))
				|| get(UID).getOwner().equals(player.getName());
	}

	/**
	 * Renames an npc.
	 * 
	 * @param UID
	 * @param changeTo
	 * @param owner
	 */
	public static void rename(int UID, String changeTo, String owner) {
		HumanNPC npc = get(UID);
		npc.getNPCData().setName(changeTo);
		removeForRespawn(UID);
		register(UID, owner);
	}

	/**
	 * Sets the colour of an npc's name.
	 * 
	 * @param UID
	 * @param owner
	 */
	public static void setColour(int UID, String owner) {
		removeForRespawn(UID);
		register(UID, owner);
	}

	public static void safeDespawn(HumanNPC npc) {
		NPCSpawner.despawnNPC(npc);
	}
}