package com.fullwall.Citizens.Utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class PropertyPool {
	public static Logger log = Logger.getLogger("Minecraft");
	public static final PropertyHandler settings = new PropertyHandler(
			"plugins/Citizens/Citizens.settings");
	public static final PropertyHandler economy = new PropertyHandler(
			"plugins/Citizens/Citizens.economy");
	public static final PropertyHandler itemlookups = new PropertyHandler(
			"plugins/Citizens/Citizens.itemlookup");
	public static final PropertyHandler texts = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.texts");
	public static final PropertyHandler locations = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.locations");
	public static final PropertyHandler colours = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.colours");
	public static final PropertyHandler items = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.items");
	public static final PropertyHandler owners = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.owners");
	public static final PropertyHandler talkWhenClose = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.talkWhenClose");
	public static final PropertyHandler lookat = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.lookat");

	public static Location getActualLocationFromName(int UID) {
		String[] values = PropertyPool.locations.getString(UID).split(",");
		if (values.length != 6) {
			log.info("getLocationFromName didn't have 6 values in values variable! Length:"
					+ values.length);
			return null;
		} else {
			Location loc = new Location(Citizens.plugin.getServer().getWorld(
					values[0]), NPCManager.getNPC(UID).getX(), NPCManager
					.getNPC(UID).getY(), NPCManager.getNPC(UID).getZ(),
					Float.parseFloat(values[4]), Float.parseFloat(values[5]));
			return loc;
		}
	}

	public static Location getLocationFromID(int UID) {
		String[] values = PropertyPool.locations.getString(UID).split(",");
		if (values.length != 6) {
			log.info("getLocationFromName didn't have 6 values in values variable! Length:"
					+ values.length);
			return null;
		} else {
			Location loc = new Location(Citizens.plugin.getServer().getWorld(
					values[0]), Double.parseDouble(values[1]),
					Double.parseDouble(values[2]),
					Double.parseDouble(values[3]), Float.parseFloat(values[4]),
					Float.parseFloat(values[5]));
			return loc;
		}
	}

	public static void saveLocation(String name, Location loc, int UID) {
		String location = loc.getWorld().getName() + "," + loc.getX() + ","
				+ loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + ","
				+ loc.getPitch();
		locations.setString(UID, location);
		if (!locations.getString("list").contains("" + UID + "_" + name))
			locations.setString("list", locations.getString("list") + "" + UID
					+ "_" + name + ",");
	}

	public static ArrayList<Integer> getItems(int UID) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		String current = items.getString(UID);
		if (current.isEmpty()) {
			current = "0,0,0,0,0,";
			items.setString(UID, current);
		}
		for (String s : current.split(",")) {
			array.add(Integer.parseInt(s));
		}
		return array;
	}

	public static void saveItems(int UID, ArrayList<Integer> items2) {
		String toSave = "";
		for (Integer i : items2) {
			toSave += "" + i + ",";
		}
		items.setString(UID, toSave);
	}

	public static String getColour(int UID) {
		return colours.getString(UID);
	}

	public static void saveColour(int UID, String colour) {
		colours.setString(UID, "" + colour);
	}

	public static String getDefaultText() {
		String[] split = settings.getString("default-text").split(";");
		String text;
		if (split != null) {
			text = split[new Random(System.currentTimeMillis())
					.nextInt(split.length)];
			if (text == null)
				text = "";
		} else
			text = "";
		return text.replace('&', '§');
	}

	public static ArrayList<String> getText(int UID) {
		String current = texts.getString(UID);
		if (!current.isEmpty()) {
			ArrayList<String> text = new ArrayList<String>();
			for (String string : current.split(";")) {
				text.add(string);
			}
			return text;
		} else
			return null;
	}

	public static void getSetText(int UID) {
		String current = texts.getString(UID);
		if (!current.isEmpty()) {
			ArrayList<String> text = new ArrayList<String>();
			for (String string : current.split(";")) {
				text.add(string);
			}
			NPCManager.setBasicNPCText(UID, text);
		}
	}

	public static void saveText(int UID, ArrayList<String> text) {
		String adding = "";
		if (text != null) {
			for (String string : text) {
				adding += string + ";";
			}
		}
		texts.setString(UID, adding);
	}

	public static boolean getNPCLookWhenClose(int UID) {
		return lookat.getBoolean(UID);
	}

	public static void setNPCLookWhenClose(int UID, boolean look) {
		lookat.setBoolean(UID, look);
	}

	public static void saveLookWhenClose(int UID, boolean value) {
		lookat.setBoolean(UID, value);
	}

	public static boolean getNPCTalkWhenClose(int UID) {
		return talkWhenClose.getBoolean(UID);
	}

	public static void setNPCTalkWhenClose(int UID, boolean talk) {
		talkWhenClose.setBoolean(UID, talk);
	}

	public static void saveTalkWhenClose(int UID, boolean value) {
		talkWhenClose.setBoolean(UID, value);
	}

	public static String getNPCOwner(int UID) {
		return owners.getString(UID);
	}

	public static void setNPCOwner(int UID, String name) {
		owners.setString(UID, name);
	}

	public static void addNPCOwner(int UID, String name, Player p) {
		String[] npcOwners = getNPCOwner(UID).split(",");
		for (int i = 0; i < npcOwners.length; i++) {
			if (npcOwners[i].equals(name) == true) {
				p.sendMessage(ChatColor.RED
						+ "This NPC already is owned by that player.");
				return;
			}
		}
		owners.setString(UID, getNPCOwner(UID) + "," + name);
	}

	public static int getNewNpcID() {
		if (locations.getString("currentID").isEmpty()) {
			locations.setInt("currentID", 0);
		}
		int returnResult = Integer.valueOf(locations.getString("currentID"));
		locations.setInt("currentID", (returnResult + 1));
		return returnResult;
	}

	public static boolean checkEconomyEnabled() {
		return economy.getBoolean("use-economy");
	}

	public static boolean checkiConomyEnabled() {
		return economy.getBoolean("use-iconomy");
	}

	public static int getPrice(String operation) {
		return economy.getInt(operation);
	}

	public static int getCurrencyID(String string) {
		int ID = economy.getInt(string);
		return ID == -1 ? 1 : ID;
	}

	public static void changeName(int UID, String changeFrom, String changeTo) {
		// IDs Remain the same, no need to save other settings.
		locations.setString(
				"list",
				locations.getString("list").replace(
						(UID + "_" + changeFrom + ","), ""));
	}

	public static void deleteNameFromList(String name) {
		locations.setString("list",
				locations.getString("list").replace(name + ",", ""));
	}

	public static void saveAll() {
		texts.save();
		locations.save();
		colours.save();
		owners.save();
		items.save();
		talkWhenClose.save();
		lookat.save();
	}

	public void removeFromFiles(int UID) {
		colours.removeKey(UID);
		items.removeKey(UID);
		locations.removeKey(UID);
		HumanNPC NPC = NPCManager.getNPC(UID);
		locations.setString(
				"list",
				locations.getString("list").replace(
						"" + UID + "_" + NPC.getName() + ",", ""));
		texts.removeKey(UID);
		NPCManager.BasicNPCTexts.remove(UID);
	}
}
