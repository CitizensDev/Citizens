package com.fullwall.Citizens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCList;
import com.fullwall.resources.redecouverte.NPClib.NPCSpawner;

@SuppressWarnings("unused")
public class NPCManager {

	private Citizens plugin;
	public ConcurrentHashMap<String, ArrayList<String>> BasicUIDs = new ConcurrentHashMap<String, ArrayList<String>>();
	public static ConcurrentHashMap<String, ArrayList<String>> BasicNPCTexts = new ConcurrentHashMap<String, ArrayList<String>>();
	private ConcurrentHashMap<String, ArrayList<String>> TraderUIDs = new ConcurrentHashMap<String, ArrayList<String>>();
	private ConcurrentHashMap<String, ArrayList<String>> GuardUIDs = new ConcurrentHashMap<String, ArrayList<String>>();
	public static ConcurrentHashMap<String, ArrayList<String>> GlobalUIDs = new ConcurrentHashMap<String, ArrayList<String>>();
	public Random ran = new Random(
			new Random(new Random(new Random(new Random(System
					.currentTimeMillis()).nextLong()).nextLong()).nextLong())
					.nextLong());
	private static NPCList list;

	public NPCManager(Citizens plugin) {
		this.plugin = plugin;
		NPCManager.list = new NPCList();
	}

	public enum NPCType {
		ALL, BASIC, TRADER, GUARD, QUEST, HEALER
	}

	public void registerBasicNPC(String name, NPCType type) {
		Location loc = PropertyPool.getLocationFromName(name);
		String uniqueID = generateID(NPCType.BASIC);

		String colour = StringUtils.getColourFromString(name);
		name = ChatColor.stripColor(name);
		String npcName = name;
		if (!colour.isEmpty() && !colour.equals("§f"))
			npcName = colour + name;

		HumanNPC npc = NPCSpawner.SpawnBasicHumanNpc(uniqueID, npcName,
				loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), 0.0F);

		ArrayList<Integer> items = PropertyPool.getItemsFromFile(name);
		NPCDataManager.addItems(npc, items);

		PropertyPool.getSetText(name);
		saveToFile(name, loc, colour, items);
		registerUID(type, uniqueID, name);
		list.put(uniqueID, npc);
	}

	public void registerBasicNPC(String name, Location loc, NPCType type) {
		PropertyPool.saveLocation(name, loc);
		registerBasicNPC(name, type);
	}

	public static void setBasicNPCText(String name, ArrayList<String> text) {
		BasicNPCTexts.put(name, text);
		PropertyPool.saveText(name, text);
	}

	public static ArrayList<String> getBasicNPCText(String name) {
		return BasicNPCTexts.get(name);
	}

	public void moveNPC(String uniqueID, Location loc) {
		HumanNPC npc = list.get(uniqueID);
		npc.moveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0.0F);
	}

	public static HumanNPC getNPC(String uniqueID) {
		return list.get(uniqueID);
	}

	public static HumanNPC getNPC(Entity entity) {
		return list.getBasicHumanNpc(entity);
	}

	public static NPCList getNPCList() {
		return list;
	}

	public static void rotateNPCToPlayer(HumanNPC NPC, Player player) {
		Location loc = NPC.getBukkitEntity().getLocation();
		float yaw = player.getLocation().getYaw() % 360 - 180;
		NPC.moveTo(loc.getX(), loc.getY(), loc.getZ(), yaw, loc.getPitch());
	}

	public void despawnNPC(String name, String uniqueID) {
		BasicUIDs.remove(name);
		TraderUIDs.remove(name);
		GuardUIDs.remove(name);
		GlobalUIDs.remove(name);
		NPCSpawner.RemoveBasicHumanNpc(list.get(uniqueID));
		list.remove(uniqueID);
	}

	public void removeNPC(String name, String uniqueID) {
		BasicUIDs.remove(name);
		TraderUIDs.remove(name);
		GuardUIDs.remove(name);
		GlobalUIDs.remove(name);
		NPCSpawner.RemoveBasicHumanNpc(list.get(uniqueID));
		list.remove(uniqueID);
		PropertyPool.colours.removeKey(name);
		PropertyPool.items.removeKey(name);
		PropertyPool.locations.removeKey(name);
		PropertyPool.locations.setString("list", PropertyPool.locations
				.getString("list").replace((name + ","), ""));
		PropertyPool.texts.removeKey(name);
	}

	private void saveToFile(String name, Location loc, String colour,
			ArrayList<Integer> items) {
		PropertyPool.saveLocation(name, loc);
		PropertyPool.saveColour(name, colour);
		PropertyPool.saveItems(name, items);
	}

	private void registerUID(NPCType type, String uniqueID, String name) {
		ArrayList<String> existingUIDs = new ArrayList<String>();
		switch (type) {
		case BASIC:
			if (BasicUIDs.containsKey(name))
				existingUIDs = BasicUIDs.get(name);
			existingUIDs.add(uniqueID);
			BasicUIDs.put(name, existingUIDs);
			break;
		case TRADER:
			if (TraderUIDs.containsKey(name))
				existingUIDs = TraderUIDs.get(name);
			existingUIDs.add(uniqueID);
			TraderUIDs.put(name, existingUIDs);
			break;
		case GUARD:
			if (GuardUIDs.containsKey(name))
				existingUIDs = GuardUIDs.get(name);
			existingUIDs.add(uniqueID);
			GuardUIDs.put(name, existingUIDs);
			break;
		case QUEST:
		case HEALER:
		}
		if (GlobalUIDs.containsKey(name))
			existingUIDs = GlobalUIDs.get(name);
		existingUIDs.add(uniqueID);
		GlobalUIDs.put(name, existingUIDs);
	}

	private String generateID(NPCType type) {
		boolean found = false;
		String UID = "";
		while (found != true) {
			UID = "" + ran.nextInt();
			switch (type) {
			case BASIC:
				if (!BasicUIDs.containsKey(UID)) {
					found = true;
					break;
				}
			case TRADER:
				if (!GuardUIDs.containsKey(UID)) {
					found = true;
					break;
				}
			case GUARD:
				if (!GuardUIDs.containsKey(UID)) {
					found = true;
					break;
				}
			case QUEST:
			case HEALER:
			}
		}
		return UID;
	}

	public ConcurrentHashMap<String, ArrayList<String>> getBasicUIDs() {
		return BasicUIDs;
	}
}
