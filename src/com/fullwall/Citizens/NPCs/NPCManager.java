package com.fullwall.Citizens.NPCs;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCList;
import com.fullwall.resources.redecouverte.NPClib.NPCSpawner;

public class NPCManager {

	@SuppressWarnings("unused")
	private Citizens plugin;
	public static ConcurrentHashMap<Integer, String> GlobalUIDs = new ConcurrentHashMap<Integer, String>();
	public static ConcurrentHashMap<Integer, ArrayList<String>> BasicNPCTexts = new ConcurrentHashMap<Integer, ArrayList<String>>();
	public static ConcurrentHashMap<String, Integer> NPCSelected = new ConcurrentHashMap<String, Integer>();
	public Random ran = new Random(new Random(new Random(new Random(
			System.currentTimeMillis()).nextLong()).nextLong()).nextLong());
	private static NPCList list;

	public NPCManager(Citizens plugin) {
		this.plugin = plugin;
		NPCManager.list = new NPCList();
	}

	/**
	 * Spawns a new npc and registers it.
	 * 
	 * @param name
	 * @param UID
	 * @param owner
	 */
	public void registerNPC(String name, int UID, String owner) {
		Location loc = PropertyManager.getBasicProperties().getLocationFromID(
				UID);
		// String uniqueID = generateID(NPCType.BASIC);

		String colour = PropertyManager.getBasicProperties().getColour(UID);// StringUtils.getColourFromString(name);
		name = ChatColor.stripColor(name);
		String npcName = name;
		if (!colour.isEmpty() && !colour.equals("§f"))
			npcName = colour + name;
		if (Constants.convertSlashes == true) {
			String[] brokenName = npcName.split(Constants.convertToSpaceChar);
			for (int i = 0; i < brokenName.length; i++) {
				if (i == 0)
					npcName = brokenName[i];
				else
					npcName += " " + brokenName[i];
			}
		}
		HumanNPC npc = NPCSpawner.SpawnBasicHumanNpc(UID, npcName,
				loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), 0.0F);

		ArrayList<Integer> items = PropertyManager.getBasicProperties()
				.getItems(UID);

		npc.setNPCData(new NPCData(name, UID, loc, colour, items, BasicNPCTexts
				.get(UID), PropertyManager.getBasicProperties()
				.getLookWhenClose(UID), PropertyManager.getBasicProperties()
				.getTalkWhenClose(UID), owner, npc.getBalance()));
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
	public int registerNPC(String name, Location loc, String owner) {
		int UID = PropertyManager.getBasicProperties().getNewNpcID();
		PropertyManager.getBasicProperties().saveLocation(name, loc, UID);
		PropertyManager.getBasicProperties().setLookWhenClose(UID,
				Constants.defaultFollowingEnabled);
		PropertyManager.getBasicProperties().setTalkWhenClose(UID,
				Constants.defaultTalkWhenClose);
		registerNPC(name, UID, owner);
		return UID;
	}

	/**
	 * Sets an npc's text to the given texts.
	 * 
	 * @param UID
	 * @param text
	 */
	public static void setBasicNPCText(int UID, ArrayList<String> text) {
		BasicNPCTexts.put(UID, text);
		getNPC(UID).getNPCData().setTexts(text);
	}

	/**
	 * Returns an npc's text.
	 * 
	 * @param UID
	 * @return
	 */
	public static ArrayList<String> getBasicNPCText(int UID) {
		return BasicNPCTexts.get(UID);
	}

	/**
	 * Gets an npc from a UID.
	 * 
	 * @param UID
	 * @return
	 */
	public static HumanNPC getNPC(int UID) {
		return list.get(UID);
	}

	/**
	 * Gets an npc from an entity.
	 * 
	 * @param entity
	 * @return
	 */
	public static HumanNPC getNPC(Entity entity) {
		return list.getBasicHumanNpc(entity);
	}

	/**
	 * Gets the list of npcs.
	 * 
	 * @return
	 */
	public static NPCList getNPCList() {
		return list;
	}

	/**
	 * Moves an npc to a location.
	 * 
	 * @param npc
	 * @param loc
	 */
	public void moveNPC(HumanNPC npc, Location loc) {
		npc.getNPCData().setLocation(loc);
		npc.moveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0.0F);
	}

	/**
	 * Rotates an npc.
	 * 
	 * @param npc
	 * @param player
	 */
	public static void rotateNPCToPlayer(HumanNPC npc, Player player) {
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
		npc.moveTo(loc.getX(), loc.getY(), loc.getZ(), (float) yaw - 90,
				(float) pitch);
	}

	/**
	 * Despawns an npc.
	 * 
	 * @param UID
	 */
	public void despawnNPC(int UID) {
		GlobalUIDs.remove(UID);
		NPCSpawner.RemoveBasicHumanNpc(list.get(UID));
		list.remove(UID);
	}

	/**
	 * Removes an npc.
	 * 
	 * @param UID
	 */
	public void removeNPC(int UID) {
		GlobalUIDs.remove(UID);
		NPCSpawner.RemoveBasicHumanNpc(list.get(UID));
		list.remove(UID);
		PropertyManager.remove(getNPC(UID));
	}

	/**
	 * Removes an npc, but not from the properties.
	 * 
	 * @param UID
	 */
	public static void removeNPCForRespawn(int UID) {
		NPCSpawner.RemoveBasicHumanNpc(list.get(UID));
	}

	/**
	 * Gets the global list of UIDs.
	 * 
	 * @return
	 */
	public ConcurrentHashMap<Integer, String> getBasicUIDs() {
		return GlobalUIDs;
	}

	/**
	 * Registers a UID in the global list.
	 * 
	 * @param UID
	 * @param name
	 */
	private void registerUID(int UID, String name) {
		GlobalUIDs.put(UID, name);
	}

	@SuppressWarnings("unused")
	private String generateUID() {
		boolean found = false;
		// Change this to an integer return?
		String UID = "";
		while (found != true) {
			UID = "" + ran.nextInt();
			if (!GlobalUIDs.containsKey(UID)) {
				found = true;
				break;
			}
		}
		return UID;
	}

	/**
	 * Checks if a given entity is an npc.
	 * 
	 * @param entity
	 * @return
	 */
	public static boolean isNPC(Entity entity) {
		return list.getBasicHumanNpc(entity) != null;
	}

	/**
	 * Checks if a player has an npc selected.
	 * 
	 * @param p
	 * @return
	 */
	public static boolean validateSelected(Player p) {
		if (NPCSelected.get(p.getName()) != null
				&& !NPCSelected.get(p.getName()).toString().isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the player has selected the given npc.
	 * 
	 * @param p
	 * @param npc
	 * @return
	 */
	public static boolean validateSelected(Player p, int UID) {
		if (NPCSelected.get(p.getName()) != null
				&& !NPCSelected.get(p.getName()).toString().isEmpty()) {
			if (NPCSelected.get(p.getName()).equals(UID))
				return true;
		}
		return false;
	}

	// Overloaded method to add an optional permission string parameter (admin
	// overrides).
	public static boolean validateOwnership(Player p, int UID, String permission) {
		if (Permission.generic(p,
				permission.replace("citizens.", "citizens.admin.")))
			return true;
		if (validateOwnership(p, UID))
			return true;
		return false;
	}

	/**
	 * Checks if a player owns a given npc.
	 * 
	 * @param UID
	 * @param p
	 * @return
	 */
	public static boolean validateOwnership(Player p, int UID) {
		String[] npcOwners = PropertyManager.getBasicProperties().getOwner(UID)
				.split(",");
		for (int i = 0; i < npcOwners.length; i++) {
			if (npcOwners[i].equals(p.getName()))
				return true;
		}
		return false;
	}
}
