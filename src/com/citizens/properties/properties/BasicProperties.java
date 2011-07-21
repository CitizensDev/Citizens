package com.citizens.properties.properties;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.server.InventoryPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.citizens.SettingsManager.Constant;
import com.citizens.interfaces.Saveable;
import com.citizens.npcs.NPCData;
import com.citizens.npcs.NPCDataManager;
import com.citizens.properties.PropertyManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.Messaging;
import com.citizens.utils.StringUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

public class BasicProperties extends PropertyManager implements Saveable {
	private final String name = ".basic.name";
	private final String color = ".basic.color";
	private final String items = ".basic.items";
	private final String inventory = ".basic.inventory";
	private final String location = ".basic.location";
	private final String lookWhenClose = ".basic.look-when-close";
	private final String talkWhenClose = ".basic.talk-when-close";
	private final String waypoints = ".basic.waypoints";
	private final String owner = ".basic.owner";
	private final String text = ".basic.text";

	public void saveName(int UID, String npcName) {
		profiles.setString(UID + name, npcName);
	}

	public String getName(int UID) {
		return profiles.getString(UID + name);
	}

	public Location getLocation(int UID) {
		String[] values = profiles.getString(UID + location).split(",");
		if (values.length != 6) {
			if (values[0].isEmpty()) {
				Messaging.log("Missing location for " + UID);
			} else
				Messaging.log("Invalid location length. Length: "
						+ values.length);
			return null;
		} else {
			return new Location(Bukkit.getServer().getWorld(values[0]),
					Double.parseDouble(values[1]),
					Double.parseDouble(values[2]),
					Double.parseDouble(values[3]), Float.parseFloat(values[4]),
					Float.parseFloat(values[5]));
		}
	}

	public void saveLocation(Location loc, int UID) {
		String locale = loc.getWorld().getName() + "," + loc.getX() + ","
				+ loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + ","
				+ loc.getPitch();
		profiles.forceSetString(UID + location, locale);
	}

	private void saveInventory(int UID, PlayerInventory inv) {
		StringBuilder save = new StringBuilder();
		int count = 0;
		for (ItemStack i : inv.getContents()) {
			if (i == null || i.getType() == Material.AIR) {
				++count;
			} else {
				if (count > 0) {
					save.append("AIR*" + count + ",");
					count = 0;
				}
				save.append(i.getTypeId())
						.append("/")
						.append(i.getAmount())
						.append("/")
						.append((i.getData() == null) ? 0 : i.getData()
								.getData()).append(",");
			}
		}
		if (count > 0) {
			save.append("AIR*" + count + ",");
			count = 0;
		}
		profiles.setString(UID + inventory, save.toString());
	}

	private PlayerInventory getInventory(int UID) {
		String save = profiles.getString(UID + inventory);
		if (save.isEmpty()) {
			return null;
		}
		ArrayList<ItemStack> array = new ArrayList<ItemStack>();
		for (String s : save.split(",")) {
			String[] split = s.split("/");
			if (!split[0].contains("AIR") && !split[0].equals("0")) {
				array.add(new ItemStack(StringUtils.parse(split[0]),
						StringUtils.parse(split[1]), (short) 0,
						(byte) StringUtils.parse(split[2])));
			} else {
				if (split[0].equals("AIR")) {
					array.add(null);
				} else {
					int count = Integer.parseInt(split[0].split("\\*")[1]);
					while (count != 0) {
						array.add(null);
						--count;
					}

				}
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
		String current = profiles.getString(UID + items);
		if (current.isEmpty()) {
			current = "0,0,0,0,0,";
			profiles.setString(UID + items, current);
		}
		for (String s : current.split(",")) {
			array.add(Integer.parseInt(s));
		}
		return array;
	}

	private void saveItems(int UID, ArrayList<Integer> items) {
		profiles.setString(UID + this.items,
				Joiner.on(",").join(items.toArray()));
	}

	public int getColour(int UID) {
		if (Strings.isNullOrEmpty(profiles.getString(UID + color))) {
			profiles.setInt(UID + color, 0xF);
			return 0xF;
		}
		try {
			return Integer.parseInt(profiles.getString(UID + color), 16);
		} catch (NumberFormatException ex) {
			return 0xF;
		}
	}

	private void saveColour(int UID, int colour) {
		profiles.setInt(UID + color, colour);
	}

	public ArrayDeque<String> getText(int UID) {
		String current = profiles.getString(UID + text);
		if (!current.isEmpty()) {
			ArrayDeque<String> texts = new ArrayDeque<String>(
					Arrays.asList(current.split(";")));
			return texts;
		}
		return null;
	}

	private void saveText(int UID, ArrayDeque<String> texts) {
		if (texts == null)
			return;
		profiles.setString(UID + text,
				Joiner.on(";").skipNulls().join(texts.toArray()));
	}

	public boolean getLookWhenClose(int UID) {
		return profiles.getBoolean(UID + lookWhenClose,
				Constant.DefaultFollowingEnabled.toBoolean());
	}

	public void saveLookWhenClose(int UID, boolean value) {
		profiles.setBoolean(UID + lookWhenClose, value);
	}

	public boolean getTalkWhenClose(int UID) {
		return profiles.getBoolean(UID + talkWhenClose,
				Constant.DefaultTalkWhenClose.toBoolean());
	}

	public void saveTalkWhenClose(int UID, boolean value) {
		profiles.setBoolean(UID + talkWhenClose, value);
	}

	public String getOwner(int UID) {
		return profiles.getString(UID + owner);
	}

	public void setOwner(int UID, String name) {
		profiles.setString(UID + owner, name);
	}

	private void saveWaypoints(int UID, List<Location> waypoints) {
		String write = "", temp = "";
		for (Location loc : waypoints) {
			temp = loc.getBlockX() + "," + loc.getBlockY() + ","
					+ loc.getBlockZ() + ";";
			write += temp;
		}
		profiles.setString(UID + this.waypoints, write);
	}

	private List<Location> getWaypoints(int UID, World world) {
		String read = profiles.getString(UID + waypoints);
		List<Location> temp = new ArrayList<Location>();
		if (!read.isEmpty()) {
			for (String str : read.split(";")) {
				String[] split = str.split(",");
				temp.add(new Location(world, Double.parseDouble(split[0]),
						Double.parseDouble(split[1]), Double
								.parseDouble(split[2])));
			}
		}
		return temp;
	}

	@Override
	public void saveState(HumanNPC npc) {
		int UID = npc.getUID();
		NPCData npcdata = npc.getNPCData();

		saveName(npc.getUID(), npcdata.getName());
		saveLocation(npcdata.getLocation(), UID);
		saveColour(UID, npcdata.getColour());
		saveItems(UID, npcdata.getItems());
		saveInventory(UID, npc.getPlayer().getInventory());
		saveText(UID, npcdata.getTexts());
		saveLookWhenClose(UID, npcdata.isLookClose());
		saveTalkWhenClose(UID, npcdata.isTalkClose());
		saveWaypoints(UID, npc.getWaypoints().getWaypoints());
		setOwner(UID, npcdata.getOwner());
	}

	@Override
	public void loadState(HumanNPC npc) {
		int UID = npc.getUID();
		NPCData npcdata = npc.getNPCData();

		npcdata.setName(getName(UID));
		npcdata.setLocation(getLocation(UID));
		npcdata.setColour(getColour(UID));
		npcdata.setItems(getItems(UID));
		npcdata.setTexts(getText(UID));
		npcdata.setLookClose(getLookWhenClose(UID));
		npcdata.setTalkClose(getTalkWhenClose(UID));
		npcdata.setOwner(getOwner(UID));
		npc.getWaypoints().setPoints(getWaypoints(UID, npc.getWorld()));
		if (getInventory(npc.getUID()) != null) {
			npc.getInventory().setContents(
					getInventory(npc.getUID()).getContents());
		}

		NPCDataManager.addItems(npc, npcdata.getItems());
		saveState(npc);
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
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + name)) {
			profiles.setString(nextUID + name, profiles.getString(UID + name));
		}
		if (profiles.pathExists(UID + text)) {
			profiles.setString(nextUID + text, profiles.getString(UID + text));
		}
		if (profiles.pathExists(UID + location)) {
			profiles.setString(nextUID + location,
					profiles.getString(UID + location));
		}
		if (profiles.pathExists(UID + color)) {
			profiles.setString(nextUID + color, profiles.getString(UID + color));
		}
		if (profiles.pathExists(UID + owner)) {
			profiles.setString(nextUID + owner, profiles.getString(UID + owner));
		}
		if (profiles.pathExists(UID + items)) {
			profiles.setString(nextUID + items, profiles.getString(UID + items));
		}
		if (profiles.pathExists(UID + inventory)) {
			profiles.setString(nextUID + inventory,
					profiles.getString(UID + inventory));
		}
		if (profiles.pathExists(UID + talkWhenClose)) {
			profiles.setString(nextUID + talkWhenClose,
					profiles.getString(UID + talkWhenClose));
		}
		if (profiles.pathExists(UID + lookWhenClose)) {
			profiles.setString(nextUID + lookWhenClose,
					profiles.getString(UID + lookWhenClose));
		}
	}

	public int getNewNpcID() {
		int count = 0;
		while (profiles.pathExists(count))
			++count;
		return count;
	}
}