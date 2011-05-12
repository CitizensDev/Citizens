package com.fullwall.Citizens.Utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.NPCs.NPCData;
import com.fullwall.Citizens.NPCs.NPCManager;

public class PropertyPool {
	public static Logger log = Logger.getLogger("Minecraft");
	public static final PropertyHandler colours = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.colours");
	public static final PropertyHandler economy = new PropertyHandler(
			"plugins/Citizens/Citizens.economy");
	public static final PropertyHandler itemlookups = new PropertyHandler(
			"plugins/Citizens/Citizens.itemlookup");
	public static final PropertyHandler items = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.items");
	public static final PropertyHandler locations = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.locations");
	public static final PropertyHandler lookat = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.lookat");
	public static final PropertyHandler owners = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.owners");
	public static final PropertyHandler settings = new PropertyHandler(
			"plugins/Citizens/Citizens.settings");
	public static final PropertyHandler talkwhenclose = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.talkWhenClose");
	public static final PropertyHandler texts = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.texts");
	public static final PropertyHandler counts = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.counts");
	public static final PropertyHandler balances = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.balances");

	public static int getMaxNPCsPerPlayer() {
		return settings.getInt("max-NPCs-per-player");
	}

	public static int getNPCAmountPerPlayer(String name) {
		return counts.getInt(name);
	}

	public static void saveNPCAmountPerPlayer(String name, int totalNPCs) {
		counts.setInt(name, totalNPCs);
	}

	public static Location getActualLocationFromName(int UID) {
		String[] values = PropertyPool.locations.getString(UID).split(",");
		if (values.length != 6) {
			log.info("getLocationFromName didn't have 6 values in values variable! Length: "
					+ values.length);
			return null;
		} else {
			Location loc = new Location(Bukkit.getServer().getWorld(values[0]),
					NPCManager.getNPC(UID).getX(), NPCManager.getNPC(UID)
							.getY(), NPCManager.getNPC(UID).getZ(),
					Float.parseFloat(values[4]), Float.parseFloat(values[5]));
			return loc;
		}
	}

	public static Location getLocationFromID(int UID) {
		String[] values = PropertyPool.locations.getString(UID).split(",");
		if (values.length != 6) {
			log.info("getLocationFromName didn't have 6 values in values variable! Length: "
					+ values.length);
			return null;
		} else {
			Location loc = new Location(Bukkit.getServer().getWorld(values[0]),
					Double.parseDouble(values[1]),
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

	public static boolean getLookWhenClose(int UID) {
		return lookat.getBoolean(UID);
	}

	public static void setLookWhenClose(int UID, boolean look) {
		lookat.setBoolean(UID, look);
	}

	public static void saveLookWhenClose(int UID, boolean value) {
		lookat.setBoolean(UID, value);
	}

	public static boolean getTalkWhenClose(int UID) {
		return talkwhenclose.getBoolean(UID);
	}

	public static void setTalkWhenClose(int UID, boolean talk) {
		talkwhenclose.setBoolean(UID, talk);
	}

	public static void saveTalkWhenClose(int UID, boolean value) {
		talkwhenclose.setBoolean(UID, value);
	}

	public static String getOwner(int UID) {
		return owners.getString(UID);
	}

	public static void setOwner(int UID, String name) {
		owners.setString(UID, name);
	}

	public static void addOwner(int UID, String name, Player p) {
		String[] npcOwners = getOwner(UID).split(",");
		for (int i = 0; i < npcOwners.length; i++) {
			if (npcOwners[i].equals(name) == true) {
				p.sendMessage(ChatColor.RED
						+ "This NPC already is owned by that player.");
				return;
			}
		}
		owners.setString(UID, getOwner(UID) + "," + name);
	}

	public static int getNewNpcID() {
		if (locations.getString("currentID").isEmpty()) {
			locations.setInt("currentID", 0);
		}
		int returnResult = Integer.valueOf(locations.getString("currentID"));
		locations.setInt("currentID", (returnResult + 1));
		return returnResult;
	}

	public static double getPrice(String operation) {
		return economy.getDouble(operation);
	}

	public static int getCurrencyID(String string) {
		int ID = economy.getInt(string);
		return ID == -1 ? 1 : ID;
	}

	public static void saveBalance(int UID, double balance) {
		balances.setDouble(UID, balance);
	}

	public static double getBalance(int UID) {
		return balances.getDouble(UID);
	}

	public static boolean checkEconomyEnabled() {
		return economy.getBoolean("use-economy");
	}

	public static boolean checkServerEconomyEnabled() {
		return economy.getBoolean("use-econplugin");
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
		talkwhenclose.save();
		lookat.save();
		counts.save();
		balances.save();
	}

	public static void removeFromFiles(String name, String playerName, int UID) {
		colours.removeKey(UID);
		items.removeKey(UID);
		locations.removeKey(UID);
		locations.setString(
				"list",
				locations.getString("list").replace(
						"" + UID + "_" + name + ",", ""));
		owners.removeKey(UID);
		lookat.removeKey(UID);
		talkwhenclose.removeKey(UID);
		texts.removeKey(UID);
		if (counts.keyExists(playerName))
			counts.setInt(playerName, counts.getInt(playerName) - 1);
		balances.removeKey(UID);
	}

	public static void saveState(int UID, NPCData npcdata) {
		saveBalance(UID, npcdata.getBalance());
		saveLocation(npcdata.getName(), npcdata.getLocation(), UID);
		saveColour(UID, npcdata.getColour());
		saveItems(UID, npcdata.getItems());
		saveText(UID, npcdata.getTexts());
		saveLookWhenClose(UID, npcdata.isLookClose());
		saveTalkWhenClose(UID, npcdata.isTalkClose());
		setOwner(UID, npcdata.getOwner());
	}

	/**
	 * Copies all data from one ID to another.
	 * 
	 * @param UID
	 * @param nextUID
	 */
	public static void copyProperties(int UID, int nextUID) {
		if (texts.keyExists(UID))
			texts.setString(nextUID, texts.getString(UID));
		if (locations.keyExists(UID))
			locations.setString(nextUID, locations.getString(UID));
		if (colours.keyExists(UID))
			colours.setString(nextUID, colours.getString(UID));
		if (owners.keyExists(UID))
			owners.setString(nextUID, owners.getString(UID));
		if (items.keyExists(UID))
			items.setString(nextUID, items.getString(UID));
		if (talkwhenclose.keyExists(UID))
			talkwhenclose.setString(nextUID, talkwhenclose.getString(UID));
		if (lookat.keyExists(UID))
			lookat.setString(nextUID, lookat.getString(UID));
		if (counts.keyExists(UID))
			counts.setString(nextUID, counts.getString(UID));
		if (balances.keyExists(UID))
			balances.setString(nextUID, balances.getString(UID));
		saveAll();
	}
}