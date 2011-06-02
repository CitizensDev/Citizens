package com.fullwall.Citizens.Properties.Properties;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

import net.minecraft.server.InventoryPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.NPCs.NPCData;
import com.fullwall.Citizens.NPCs.NPCDataManager;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyHandler;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BasicProperties extends Saveable {
	public static Logger log = Logger.getLogger("Minecraft");
	public final PropertyHandler colours = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.colours");
	public final PropertyHandler items = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.items");
	public final PropertyHandler inventories = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/inventories.citizens");
	public final PropertyHandler locations = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.locations");
	public final PropertyHandler lookat = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.lookat");
	public final PropertyHandler owners = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.owners");
	public final PropertyHandler talkwhenclose = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.talkWhenClose");
	public final PropertyHandler texts = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.texts");
	public final PropertyHandler counts = new PropertyHandler(
			"plugins/Citizens/Basic NPCs/Citizens.counts");
	public final PropertyHandler balances = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.balances");

	public int getNPCAmountPerPlayer(String name) {
		return counts.getInt(name);
	}

	public void saveNPCAmountPerPlayer(String name, int totalNPCs) {
		counts.setInt(name, totalNPCs);
	}

	public Location getActualLocationFromName(int UID) {
		String[] values = locations.getString(UID).split(",");
		if (values.length != 6) {
			log.info("getLocationFromName didn't have 6 values in values variable! Length: "
					+ values.length);
			return null;
		} else {
			Location loc = new Location(Bukkit.getServer().getWorld(values[0]),
					NPCManager.get(UID).getX(), NPCManager.get(UID).getY(),
					NPCManager.get(UID).getZ(), Float.parseFloat(values[4]),
					Float.parseFloat(values[5]));
			return loc;
		}
	}

	public Location getLocation(int UID) {
		String[] values = locations.getString(UID).split(",");
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

	public void saveLocation(String name, Location loc, int UID) {
		String location = loc.getWorld().getName() + "," + loc.getX() + ","
				+ loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + ","
				+ loc.getPitch();
		locations.setString(UID, location);
		if (!locations.getString("list").contains("" + UID + "_" + name))
			locations.setString("list", locations.getString("list") + "" + UID
					+ "_" + name + ",");
	}

	private void saveInventory(int UID, PlayerInventory inv) {
		String save = "";
		for (ItemStack i : inv.getContents()) {
			if (i == null || i.getType() == Material.AIR) {
				save += "AIR,";
			} else {
				save += i.getTypeId() + "/" + i.getAmount() + "/"
						+ ((i.getData() == null) ? 0 : i.getData().getData())
						+ ",";
			}
		}
		inventories.setString(UID, save);
	}

	private PlayerInventory getInventory(int UID) {
		String save = inventories.getString(UID);
		if (save.isEmpty()) {
			return null;
		}
		ArrayList<ItemStack> array = new ArrayList<ItemStack>();
		for (String s : save.split(",")) {
			String[] split = s.split("/");
			if (!split[0].equals("AIR") && !split[0].equals("0")) {
				array.add(new ItemStack(StringUtils.parse(split[0]),
						StringUtils.parse(split[1]), (short) 0,
						(byte) StringUtils.parse(split[2])));
			} else {
				array.add(null);
			}
		}
		PlayerInventory inv = new CraftInventoryPlayer(
				new InventoryPlayer(null));
		ItemStack[] stacks = inv.getContents();
		inv.setContents(array.toArray(stacks));
		return inv;
	}

	public ArrayList<Integer> getItems(int UID) {
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

	private void saveItems(int UID, ArrayList<Integer> items2) {
		String toSave = "";
		for (Integer i : items2) {
			toSave += "" + i + ",";
		}
		items.setString(UID, toSave);
	}

	public int getColour(int UID) {
		try {
			return colours.getInt(UID, 0xf);
		} catch (NumberFormatException ex) {
			int colour = 0xf;
			if (colours.keyExists(UID) && !colours.getString(UID).isEmpty()) {
				try {
					colour = Integer.parseInt(""
							+ colours.getString(UID).charAt(
									colours.getString(UID).length() - 1));
				} catch (NumberFormatException e) {
					try {
						colour = Integer.parseInt(
								""
										+ colours.getString(UID)
												.charAt(colours.getString(UID)
														.length() - 1), 16);
					} catch (NumberFormatException exa) {
					}
				}
			}
			saveColour(UID, colour);
			return colour;
		}
	}

	private void saveColour(int UID, int colour) {
		colours.setInt(UID, colour);
	}

	public LinkedList<String> getText(int UID) {
		String current = texts.getString(UID);
		if (!current.isEmpty()) {
			LinkedList<String> text = new LinkedList<String>();
			for (String string : current.split(";")) {
				text.add(string);
			}
			return text;
		} else {
			return null;
		}
	}

	public void getSetText(int UID) {
		String current = texts.getString(UID);
		if (!current.isEmpty()) {
			LinkedList<String> text = new LinkedList<String>();
			for (String string : current.split(";")) {
				text.add(string);
			}
			NPCManager.setText(UID, text);
		}
	}

	private void saveText(int UID, LinkedList<String> text) {
		String adding = "";
		if (text != null) {
			for (String string : text) {
				adding += string + ";";
			}
		}
		texts.setString(UID, adding);
	}

	public boolean getLookWhenClose(int UID) {
		return lookat.getBoolean(UID, Constants.defaultFollowingEnabled);
	}

	public void saveLookWhenClose(int UID, boolean value) {
		lookat.setBoolean(UID, value);
	}

	public boolean getTalkWhenClose(int UID) {
		return talkwhenclose.getBoolean(UID, Constants.defaultTalkWhenClose);
	}

	public void saveTalkWhenClose(int UID, boolean value) {
		talkwhenclose.setBoolean(UID, value);
	}

	public String getOwner(int UID) {
		return owners.getString(UID);
	}

	private void setOwner(int UID, String name) {
		owners.setString(UID, name);
	}

	public int getNewNpcID() {
		if (locations.getString("currentID").isEmpty()) {
			locations.setInt("currentID", 0);
		}
		int returnResult = Integer.valueOf(locations.getString("currentID"));
		locations.setInt("currentID", (returnResult + 1));
		return returnResult;
	}

	private void saveBalance(int UID, double balance) {
		balances.setDouble(UID, balance);
	}

	private double getBalance(int UID) {
		return balances.getDouble(UID);
	}

	public void changeName(int UID, String changeFrom, String changeTo) {
		// IDs Remain the same, no need to save other settings.
		locations.setString(
				"list",
				locations.getString("list").replace(
						(UID + "_" + changeFrom + ","), ""));
	}

	public void deleteNameFromList(String name) {
		locations.setString("list",
				locations.getString("list").replace(name + ",", ""));
	}

	@Override
	public void saveFiles() {
		texts.save();
		locations.save();
		colours.save();
		owners.save();
		items.save();
		inventories.save();
		talkwhenclose.save();
		lookat.save();
		counts.save();
		balances.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		int UID = npc.getUID();
		NPCData npcdata = npc.getNPCData();

		saveBalance(UID, npc.getBalance());
		saveLocation(npcdata.getName(), npcdata.getLocation(), UID);
		saveColour(UID, npcdata.getColour());
		saveItems(UID, npcdata.getItems());
		saveInventory(npc.getUID(), npc.getPlayer().getInventory());
		saveText(UID, npcdata.getTexts());
		saveLookWhenClose(UID, npcdata.isLookClose());
		saveTalkWhenClose(UID, npcdata.isTalkClose());
		setOwner(UID, npcdata.getOwner());
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setBalance(getBalance(npc.getUID()));

		int UID = npc.getUID();
		NPCData npcdata = npc.getNPCData();

		npcdata.setLocation(getLocation(UID));
		npcdata.setColour(getColour(UID));
		npcdata.setItems(getItems(UID));
		npcdata.setTexts(getText(UID));
		npcdata.setLookClose(getLookWhenClose(UID));
		npcdata.setTalkClose(getTalkWhenClose(UID));
		npcdata.setOwner(getOwner(UID));
		if (getInventory(npc.getUID()) != null) {
			npc.getInventory().setContents(
					getInventory(npc.getUID()).getContents());
		}

		NPCDataManager.addItems(npc, npcdata.getItems());
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		colours.removeKey(npc.getUID());
		items.removeKey(npc.getUID());
		inventories.removeKey(npc.getUID());
		locations.removeKey(npc.getUID());
		locations.setString(
				"list",
				locations.getString("list").replace(
						"" + npc.getUID() + "_" + npc.getStrippedName() + ",",
						""));
		owners.removeKey(npc.getUID());
		lookat.removeKey(npc.getUID());
		talkwhenclose.removeKey(npc.getUID());
		texts.removeKey(npc.getUID());
		if (counts.keyExists(npc.getOwner())) {
			counts.setInt(npc.getOwner(), counts.getInt(npc.getOwner()) - 1);
		}
		balances.removeKey(npc.getUID());
		if (locations.getString("list").isEmpty()) {
			locations.removeKey("list");
			locations.setInt("currentID", 0);
		}
	}

	@Override
	public void register(HumanNPC npc) {
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return true;
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return true;
	}

	@Override
	public PropertyType type() {
		return PropertyType.BASIC;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (texts.keyExists(UID)) {
			texts.setString(nextUID, texts.getString(UID));
		}
		if (locations.keyExists(UID)) {
			locations.setString(nextUID, locations.getString(UID));
		}
		if (colours.keyExists(UID)) {
			colours.setString(nextUID, colours.getString(UID));
		}
		if (owners.keyExists(UID)) {
			owners.setString(nextUID, owners.getString(UID));
		}
		if (items.keyExists(UID)) {
			items.setString(nextUID, items.getString(UID));
		}
		if (inventories.keyExists(UID)) {
			inventories.setString(nextUID, inventories.getString(UID));
		}
		if (talkwhenclose.keyExists(UID)) {
			talkwhenclose.setString(nextUID, talkwhenclose.getString(UID));
		}
		if (lookat.keyExists(UID)) {
			lookat.setString(nextUID, lookat.getString(UID));
		}
		if (counts.keyExists(UID)) {
			counts.setString(nextUID, counts.getString(UID));
		}
		if (balances.keyExists(UID)) {
			balances.setString(nextUID, balances.getString(UID));
		}
	}
}