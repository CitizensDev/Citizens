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
	public static ConcurrentHashMap<String, String> NPCSelected = new ConcurrentHashMap<String, String>();
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

	public void registerBasicNPC(String name, NPCType type, int UID) {
		Location loc = PropertyPool.getLocationFromID(UID);
		//String uniqueID = generateID(NPCType.BASIC);

		String colour = PropertyPool.getColour(UID);//StringUtils.getColourFromString(name);
		name = ChatColor.stripColor(name);
		String npcName = name;
		if (!colour.isEmpty() && !colour.equals("§f"))
			npcName = colour + name;
		
		if(Citizens.convertUnderscores == true){
			String[] brokenName = npcName.split("_");
			for(int i = 0; i < brokenName.length; i++){
				if(i == 0) npcName = brokenName[i];
				else npcName += " " + brokenName[i];
			}
		}

		HumanNPC npc = NPCSpawner.SpawnBasicHumanNpc(""+UID, npcName,
				loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), 0.0F, UID);

		ArrayList<Integer> items = PropertyPool.getItemsFromFile(UID);
		NPCDataManager.addItems(npc, items);

		PropertyPool.getSetText(UID);
		saveToFile(name, loc, colour, items, UID);
		registerUID(type, ""+UID, name);
		list.put(""+UID, npc);
	}

	public int registerBasicNPC(String name, Location loc, NPCType type) {
		int UID = PropertyPool.getNewNpcID();
		PropertyPool.saveLocation(name, loc, UID);
		registerBasicNPC(name, type, UID);
		return UID;
	}

	public static void setBasicNPCText(int UID, ArrayList<String> text) {
		BasicNPCTexts.put(""+UID, text);
		PropertyPool.saveText(UID, text);
	}

	public static ArrayList<String> getBasicNPCText(int UID) {
		return BasicNPCTexts.get(""+UID);
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
        double xDiff = player.getLocation().getX() - loc.getX();
        double yDiff = player.getLocation().getY() - loc.getY();
        double zDiff = player.getLocation().getZ() - loc.getZ();
        double DistanceXZ = Math.sqrt(xDiff*xDiff+zDiff*zDiff);
        double DistanceY = Math.sqrt(DistanceXZ*DistanceXZ+yDiff*yDiff);
        double yaw = (Math.acos(xDiff/DistanceXZ)*180/Math.PI);
        double pitch = (Math.acos(yDiff/DistanceY)*180/Math.PI)-90;
        if(zDiff < 0.0){
                yaw = yaw + (Math.abs(180 - yaw)*2);
        }
        NPC.moveTo(loc.getX(), loc.getY(), loc.getZ(), (float)yaw-90, (float)pitch);
}

	public void despawnNPC(String name, String uniqueID) {
		BasicUIDs.remove(uniqueID);
		TraderUIDs.remove(uniqueID);
		GuardUIDs.remove(uniqueID);
		GlobalUIDs.remove(uniqueID);
		NPCSpawner.RemoveBasicHumanNpc(list.get(uniqueID));
		list.remove(uniqueID);
	}

	public void removeNPC(String name, int UID) {
		BasicUIDs.remove(""+UID);
		TraderUIDs.remove(""+UID);
		GuardUIDs.remove(""+UID);
		GlobalUIDs.remove(""+UID);
		String actualName = NPCManager.getNPC(""+UID).getName();
		NPCSpawner.RemoveBasicHumanNpc(list.get(""+UID));
		list.remove(""+UID);
		PropertyPool.colours.removeKey(name);
		PropertyPool.items.removeKey(name);
		PropertyPool.locations.removeKey(""+UID);
		PropertyPool.locations.setString("list", PropertyPool.locations
				.getString("list").replace((""+UID+"_"+actualName + ","), ""));
		PropertyPool.texts.removeKey(name);
	}
	
	public void removeNPCForRespawn(int UID){
		NPCSpawner.RemoveBasicHumanNpc(list.get(""+UID));
	}

	private void saveToFile(String name, Location loc, String colour,
			ArrayList<Integer> items, int UID) {
		PropertyPool.saveLocation(name, loc, UID);
		PropertyPool.saveColour(UID, colour);
		PropertyPool.saveItems(UID, items);
	}

	private void registerUID(NPCType type, String uniqueID, String name) {
		ArrayList<String> existingUIDs = new ArrayList<String>();
		switch (type) {
		case BASIC:
			if (BasicUIDs.containsKey(uniqueID))
				existingUIDs = BasicUIDs.get(uniqueID);
			existingUIDs.add(uniqueID);
			BasicUIDs.put(uniqueID, existingUIDs);
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
