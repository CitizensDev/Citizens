package com.citizens.NPCs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.citizens.Constants;
import com.citizens.Permission;
import com.citizens.Events.NPCSpawnEvent;
import com.citizens.Properties.PropertyManager;
import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Resources.NPClib.NPCList;
import com.citizens.Resources.NPClib.NPCSpawner;
import com.citizens.Utils.StringUtils;

public class NPCManager {
	public static final Map<Integer, String> GlobalUIDs = new ConcurrentHashMap<Integer, String>();
	public static final Map<Integer, ArrayDeque<String>> NPCTexts = new ConcurrentHashMap<Integer, ArrayDeque<String>>();
	private static final Map<String, Integer> selectedNPCs = new ConcurrentHashMap<String, Integer>();
	private static NPCList list = new NPCList();

	/**
	 * Spawns a new npc and registers it.
	 * 
	 * @param UID
	 * @param owner
	 */
	public static void register(int UID, String owner) {
		Location loc = PropertyManager.getBasic().getLocation(UID);

		int colour = PropertyManager.getBasic().getColour(UID);
		String name = PropertyManager.getBasic().getName(UID);
		name = ChatColor.stripColor(name);
		String npcName = name;
		if (colour != 0xF) {
			npcName = ChatColor.getByCode(colour) + name;
		}
		if (Constants.convertSlashes) {
			String[] brokenName = npcName.split(Constants.convertToSpaceChar);
			for (int i = 0; i < brokenName.length; i++) {
				if (i == 0) {
					npcName = brokenName[i];
				} else {
					npcName += " " + brokenName[i];
				}
			}
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

		npc.setNPCData(new NPCData(npcName, UID, loc, colour, items, NPCTexts
				.get(UID), PropertyManager.getBasic().getLookWhenClose(UID),
				PropertyManager.getBasic().getTalkWhenClose(UID), owner));
		PropertyManager.getBasic().setOwner(UID, owner);
		PropertyManager.load(npc);

		registerUID(UID, npcName);
		list.put(UID, npc);

		PropertyManager.save(npc);

		npc.getPlayer().setSleepingIgnored(true);
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
				Constants.defaultFollowingEnabled);
		PropertyManager.getBasic().saveTalkWhenClose(UID,
				Constants.defaultTalkWhenClose);
		PropertyManager.getBasic().saveName(UID, name);
		register(UID, owner);
		return UID;
	}

	/**
	 * Sets an npc's text to the given texts.
	 * 
	 * @param UID
	 * @param text
	 */
	public static void setText(int UID, ArrayDeque<String> text) {
		text = StringUtils.colourise(text);
		NPCTexts.put(UID, text);
		get(UID).getNPCData().setTexts(text);
	}

	/**
	 * Returns an npc's text.
	 * 
	 * @param UID
	 * @return
	 */
	public static ArrayDeque<String> getText(int UID) {
		return NPCTexts.get(UID);
	}

	/**
	 * Resets an NPC's text.
	 * 
	 * @param UID
	 */
	public static void resetText(int UID) {
		setText(UID, new ArrayDeque<String>());
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
		NPCSpawner.despawnNPC(list.get(UID));
		list.remove(UID);
	}

	/**
	 * Removes an npc.
	 * 
	 * @param UID
	 */
	public static void remove(int UID) {
		PropertyManager.remove(get(UID));
		NPCSpawner.despawnNPC(list.get(UID));
		GlobalUIDs.remove(UID);
		list.remove(UID);
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
		NPCSpawner.despawnNPC(list.get(UID));
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
		return selectedNPCs.get(p.getName()) != null
				&& !selectedNPCs.get(p.getName()).toString().isEmpty();
	}

	/**
	 * Checks if the player has selected the given npc.
	 * 
	 * @param p
	 * @param npc
	 * @return
	 */
	public static boolean validateSelected(Player p, int UID) {
		if (selectedNPCs.get(p.getName()) != null
				&& !selectedNPCs.get(p.getName()).toString().isEmpty()) {
			if (selectedNPCs.get(p.getName()) == UID) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Overloaded method to add an optional permission string parameter (admin
	 * overrides)
	 * 
	 * @param player
	 * @param UID
	 * @param permission
	 * @return
	 */
	public static boolean validateOwnership(Player player, int UID,
			String permission) {
		return Permission.generic(player,
				permission.replace("citizens.", "citizens.admin."))
				|| validateOwnership(player, UID);
	}

	/**
	 * Checks if a player owns a given npc.
	 * 
	 * @param UID
	 * @param p
	 * @return
	 */
	public static boolean validateOwnership(Player player, int UID) {
		return get(UID).getOwner().equals(player.getName());
	}

	/**
	 * Renames an npc.
	 * 
	 * @param UID
	 * @param changeTo
	 * @param owner
	 */
	public static void rename(int UID, String changeTo, String owner) {
		HumanNPC n = get(UID);
		PropertyManager.remove(n);
		n.getNPCData().setName(changeTo);
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

	/**
	 * Adds to an npc's text.
	 * 
	 * @param UID
	 * @param text
	 */
	public static void addText(int UID, String text) {
		ArrayDeque<String> texts = getText(UID);
		if (texts == null) {
			texts = new ArrayDeque<String>();
		}
		texts.add(text);
		setText(UID, texts);
	}

	/**
	 * Despawns all npcs.
	 */
	public static void despawnAll() {
		for (Integer i : GlobalUIDs.keySet()) {
			despawn(i);
		}
	}

	public static int getSelected(Player player) {
		return selectedNPCs.get(player.getName());
	}

	public static void selectNPC(Player player, HumanNPC npc) {
		selectedNPCs.put(player.getName(), npc.getUID());
	}

	public static void deselectNPC(Player player) {
		selectedNPCs.remove(player.getName());
	}
}