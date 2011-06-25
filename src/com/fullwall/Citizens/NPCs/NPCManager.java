package com.fullwall.Citizens.NPCs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Events.NPCSpawnEvent;
import com.fullwall.Citizens.Interfaces.NPCFactory;
import com.fullwall.Citizens.Interfaces.NPCType;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCList;
import com.fullwall.resources.redecouverte.NPClib.NPCSpawner;

public class NPCManager {
	public static final ConcurrentHashMap<Integer, String> GlobalUIDs = new ConcurrentHashMap<Integer, String>();
	public static final ConcurrentHashMap<Integer, ArrayDeque<String>> NPCTexts = new ConcurrentHashMap<Integer, ArrayDeque<String>>();
	public static final ConcurrentHashMap<String, Integer> selectedNPCs = new ConcurrentHashMap<String, Integer>();
	public static final HashMap<String, Integer> pathEditors = new HashMap<String, Integer>();
	public static final HashMap<String, NPCType> types = new HashMap<String, NPCType>();
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

		npc.setNPCData(new NPCData(name, UID, loc, colour, items, NPCTexts
				.get(UID), PropertyManager.getBasic().getLookWhenClose(UID),
				PropertyManager.getBasic().getTalkWhenClose(UID), owner));
		PropertyManager.getBasic().setOwner(UID, owner);
		PropertyManager.load(npc);

		registerUID(UID, name);
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
		Location loc = npc.getLocation();
		double xDiff = player.getLocation().getX() - loc.getX();
		double yDiff = player.getLocation().getY() - loc.getY();
		double zDiff = player.getLocation().getZ() - loc.getZ();
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
		GlobalUIDs.remove(UID);
		NPCSpawner.despawnNPC(list.get(UID));
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
	public ConcurrentHashMap<Integer, String> getUIDs() {
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

	public static void handlePathEditor(PlayerInteractEvent event) {
		String name = event.getPlayer().getName();
		if (pathEditors.get(name) != null) {
			HumanNPC npc = get(pathEditors.get(name));
			switch (event.getAction()) {
			case LEFT_CLICK_BLOCK:
				Location loc = event.getClickedBlock().getLocation();
				npc.getWaypoints().add(loc);
				event.getPlayer().sendMessage(
						StringUtils.wrap("Added") + " waypoint at ("
								+ StringUtils.wrap(loc.getBlockX()) + ", "
								+ StringUtils.wrap(loc.getBlockY()) + ", "
								+ StringUtils.wrap(loc.getBlockZ()) + ") ("
								+ StringUtils.wrap(npc.getWaypoints().size())
								+ " waypoints)");
				break;
			case RIGHT_CLICK_BLOCK:
			case RIGHT_CLICK_AIR:
				if (npc.getWaypoints().size() > 0) {
					npc.getWaypoints().removeLast();
					event.getPlayer().sendMessage(
							StringUtils.wrap("Undid")
									+ " the last waypoint ("
									+ StringUtils.wrap(npc.getWaypoints()
											.size()) + " remaining)");

				} else
					event.getPlayer().sendMessage(
							ChatColor.GRAY + "No more waypoints.");
				break;
			}
		}
	}

	public static int getSelected(String name) {
		return selectedNPCs.get(name);
	}

	public static NPCFactory getFactory(String string) {
		return types.get(string).factory();
	}

	public static void registerType(NPCType type) {
		types.put(type.getType(), type);
	}

	public static boolean validType(String type) {
		return types.get(type) != null;
	}

	public static NPCType getType(String type) {
		return types.get(type);
	}
}