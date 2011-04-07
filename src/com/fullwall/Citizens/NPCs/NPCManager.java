package com.fullwall.Citizens.NPCs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCList;
import com.fullwall.resources.redecouverte.NPClib.NPCSpawner;

@SuppressWarnings("unused")
public class NPCManager {

	private Citizens plugin;
	public static ConcurrentHashMap<Integer, String> GlobalUIDs = new ConcurrentHashMap<Integer, String>();
	public static ConcurrentHashMap<Integer, ArrayList<String>> BasicNPCTexts = new ConcurrentHashMap<Integer, ArrayList<String>>();
	public static ConcurrentHashMap<String, Integer> NPCSelected = new ConcurrentHashMap<String, Integer>();
	public Random ran = new Random(
			new Random(new Random(new Random(new Random(System
					.currentTimeMillis()).nextLong()).nextLong()).nextLong())
					.nextLong());
	private static NPCList list;
	public static NPCManager manager;

	public NPCManager(Citizens plugin) {
		this.plugin = plugin;
		NPCManager.list = new NPCList();
		manager = this;
	}

	public enum NPCType {
		ALL, BASIC, TRADER, GUARD, QUEST, HEALER
	}

	public void registerBasicNPC(String name, NPCType type, int UID) {
		Location loc = PropertyPool.getLocationFromID(UID);
		// String uniqueID = generateID(NPCType.BASIC);

		String colour = PropertyPool.getColour(UID);// StringUtils.getColourFromString(name);
		name = ChatColor.stripColor(name);
		String npcName = name;
		if (!colour.isEmpty() && !colour.equals("§f"))
			npcName = colour + name;
		if (Citizens.convertSlashes == true) {
			String[] brokenName = npcName.split(Citizens.convertToSpaceChar);
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

		ArrayList<Integer> items = PropertyPool.getItemsFromFile(UID);
		NPCDataManager.addItems(npc, items);

		PropertyPool.getSetText(UID);
		saveToFile(name, loc, colour, items, UID);
		registerUID(UID, name);
		npc.setType(type);
		list.put(UID, npc);
	}

	public int registerBasicNPC(String name, Location loc, NPCType type) {
		int UID = PropertyPool.getNewNpcID();
		PropertyPool.saveLocation(name, loc, UID);
		PropertyPool.setNPCLookWhenClose(UID, Citizens.defaultFollowingEnabled);
		PropertyPool.setNPCTalkWhenClose(UID, Citizens.defaultTalkWhenClose);
		registerBasicNPC(name, type, UID);
		return UID;
	}

	public static void setBasicNPCText(int UID, ArrayList<String> text) {
		BasicNPCTexts.put(UID, text);
		PropertyPool.saveText(UID, text);
	}

	public static ArrayList<String> getBasicNPCText(int UID) {
		return BasicNPCTexts.get(UID);
	}

	public static HumanNPC getNPC(int UID) {
		return list.get(UID);
	}

	public static HumanNPC getNPC(Entity entity) {
		return list.getBasicHumanNpc(entity);
	}

	public static NPCList getNPCList() {
		return list;
	}

	public void moveNPC(int UID, Location loc) {
		HumanNPC npc = list.get(UID);
		String location = loc.getWorld().getName() + "," + loc.getX() + ","
				+ loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + ","
				+ loc.getPitch();
		PropertyPool.locations.setString(UID, location);
		npc.moveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0.0F);
	}

	public static void rotateNPCToPlayer(HumanNPC NPC, Player player) {
		Location loc = NPC.getBukkitEntity().getLocation();
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
		NPC.moveTo(loc.getX(), loc.getY(), loc.getZ(), (float) yaw - 90,
				(float) pitch);
	}

	public void despawnNPC(int UID) {
		GlobalUIDs.remove(UID);
		NPCSpawner.RemoveBasicHumanNpc(list.get(UID));
		list.remove(UID);
	}

	public void removeNPC(int UID) {
		GlobalUIDs.remove(UID);
		String actualName = NPCManager.getNPC(UID).getName();
		NPCSpawner.RemoveBasicHumanNpc(list.get(UID));
		list.remove(UID);
		PropertyPool.colours.removeKey(UID);
		PropertyPool.items.removeKey(UID);
		PropertyPool.locations.removeKey(UID);
		PropertyPool.owners.removeKey(UID);
		PropertyPool.lookat.removeKey(UID);
		PropertyPool.talkWhenClose.removeKey(UID);
		PropertyPool.texts.removeKey(UID);
		PropertyPool.locations.setString(
				"list",
				PropertyPool.locations.getString("list").replace(
						("" + UID + "_" + actualName + ","), ""));
	}

	public static void removeNPCForRespawn(int UID) {
		NPCSpawner.RemoveBasicHumanNpc(list.get(UID));
	}

	public ConcurrentHashMap<Integer, String> getBasicUIDs() {
		return GlobalUIDs;
	}

	private void registerUID(int UID, String name) {
		GlobalUIDs.put(UID, name);
	}

	private String generateUID(NPCType type) {
		boolean notFound = false;
		// Change this to an integer return?
		String UID = "";
		while (notFound != true) {
			UID = "" + ran.nextInt();
			if (!GlobalUIDs.containsKey(UID)) {
				notFound = true;
				break;
			}
		}
		return UID;
	}

	private void saveToFile(String name, Location loc, String colour,
			ArrayList<Integer> items, int UID) {
		PropertyPool.saveLocation(name, loc, UID);
		PropertyPool.saveColour(UID, colour);
		PropertyPool.saveItems(UID, items);
	}

	public static boolean isNPC(Entity entity) {
		return list.getBasicHumanNpc(entity) != null;
	}

	public static boolean validateSelected(Player p) {
		if (NPCManager.NPCSelected.get(p.getName()) != null
				&& !NPCManager.NPCSelected.get(p.getName()).toString()
						.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean validateOwnership(int UID, Player p) {
		String[] npcOwners = PropertyPool.getNPCOwner(UID).split(",");
		for (int i = 0; i < npcOwners.length; i++) {
			if (npcOwners[i].equals(p.getName()))
				return true;
		}
		return false;
	}
}
