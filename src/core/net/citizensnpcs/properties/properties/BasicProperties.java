package net.citizensnpcs.properties.properties;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.Node;
import net.citizensnpcs.api.Properties;
import net.citizensnpcs.npcs.NPCData;
import net.citizensnpcs.npcs.NPCDataManager;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.waypoints.Waypoint;
import net.citizensnpcs.waypoints.WaypointModifier;
import net.citizensnpcs.waypoints.WaypointModifierType;
import net.minecraft.server.InventoryPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class BasicProperties extends PropertyManager implements Properties {
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
		List<ItemStack> array = new ArrayList<ItemStack>();
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

	// Gets a map of items
	public List<Map<Integer, Short>> getItems(int UID) {
		List<Map<Integer, Short>> map = Lists.newArrayList();
		String current = profiles.getString(UID + this.items);
		if (current.isEmpty()) {
			current = "0:0,0:0,0:0,0:0,0:0,";
			profiles.setString(UID + this.items, current);
		}
		for (String s : current.split(",")) {
			if (!s.contains(":")) {
				s += ":0";
			}
			String[] parts = s.split(":");
			HashMap<Integer, Short> items = new HashMap<Integer, Short>();
			items.put(Integer.parseInt(parts[0]), Short.parseShort(parts[1]));
			map.add(items);
		}
		return map;
	}

	private void saveItems(int UID, List<Map<Integer, Short>> items) {
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < items.size(); i++) {
			for (Entry<Integer, Short> entry : items.get(i).entrySet()) {
				short durability = entry.getValue();
				if (durability == -1) {
					durability = 0;
				}
				temp.append(entry.getKey() + ":" + durability + ",");
			}
		}
		profiles.setString(UID + this.items, temp.toString());
	}

	public ChatColor getColour(int UID) {
		if (Strings.isNullOrEmpty(profiles.getString(UID + color))) {
			profiles.setInt(UID + color, 0xF);
			return ChatColor.WHITE;
		}
		try {
			return ChatColor.getByCode(Integer.parseInt(profiles.getString(UID
					+ color)));
		} catch (NumberFormatException ex) {
			return ChatColor.getByCode(Integer.parseInt(
					profiles.getString(UID + color), 16));
		}
	}

	private void saveColour(int UID, ChatColor colour) {
		profiles.setInt(UID + color, colour == null ? ChatColor.WHITE.getCode()
				: colour.getCode());
	}

	public Deque<String> getText(int UID) {
		String current = profiles.getString(UID + text);
		if (!current.isEmpty()) {
			Deque<String> texts = new ArrayDeque<String>(Arrays.asList(current
					.split(";")));
			return texts;
		}
		return null;
	}

	private void saveText(int UID, Deque<String> texts) {
		if (texts == null) {
			return;
		}
		profiles.setString(UID + text, Joiner.on(";").skipNulls().join(texts));
	}

	public boolean isLookWhenClose(int UID) {
		return profiles.getBoolean(UID + lookWhenClose,
				SettingsManager.getBoolean("DefaultLookAt"));
	}

	public void saveLookWhenClose(int UID, boolean value) {
		profiles.setBoolean(UID + lookWhenClose, value);
	}

	public boolean isTalkWhenClose(int UID) {
		return profiles.getBoolean(UID + talkWhenClose,
				SettingsManager.getBoolean("DefaultTalkClose"));
	}

	public void saveTalkWhenClose(int UID, boolean value) {
		profiles.setBoolean(UID + talkWhenClose, value);
	}

	public String getOwner(int UID) {
		return profiles.getString(UID + owner);
	}

	public void saveOwner(int UID, String name) {
		profiles.setString(UID + owner, name);
	}

	private void saveWaypoints(int UID, List<Waypoint> list) {
		profiles.removeKey(UID + this.waypoints); // clear old waypoints.

		int count = 0, innercount = 0;
		String path = "", root = "";
		for (Waypoint waypoint : list) {
			innercount = 0;

			path = UID + this.waypoints + "." + count;
			LocationUtils.saveLocation(profiles, waypoint.getLocation(), path,
					true);

			profiles.setInt(path + ".delay", waypoint.getDelay());
			path += ".modifiers.";

			for (WaypointModifier modifier : waypoint.getModifiers()) {
				root = path + innercount;
				profiles.setString(root + ".type", modifier.getType().name());
				modifier.save(profiles, root);
				++innercount;
			}
			++count;
		}
	}

	private List<Waypoint> getWaypoints(int UID, World world) {
		List<Waypoint> temp = new ArrayList<Waypoint>();
		if (!profiles.pathExists(UID + waypoints)) {
			return temp;
		}
		String read = profiles.getString(UID + waypoints);
		Waypoint waypoint = null;
		if (!read.isEmpty()) {
			for (String str : read.split(";")) {
				String[] split = str.split(",");
				temp.add(new Waypoint(new Location(world, Double
						.parseDouble(split[0]), Double.parseDouble(split[1]),
						Double.parseDouble(split[2]))));
			}
			return temp;
		}
		String path = "", root = "";
		WaypointModifier modifier = null;
		for (String key : profiles.getKeys(UID + waypoints)) {
			root = UID + waypoints + "." + key;
			waypoint = new Waypoint(LocationUtils.loadLocation(profiles, root,
					true));

			waypoint.setDelay(profiles.getInt(root + ".delay"));

			if (profiles.pathExists(root + ".modifiers")) {
				root += ".modifiers";
				for (String innerKey : profiles.getKeys(root)) {
					path = root + "." + innerKey;
					modifier = WaypointModifierType.valueOf(
							profiles.getString(path + ".type"))
							.create(waypoint);
					modifier.parse(profiles, path);
					waypoint.addModifier(modifier);
				}
			}
			temp.add(waypoint);
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
		saveOwner(UID, npcdata.getOwner());
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
		npcdata.setLookClose(isLookWhenClose(UID));
		npcdata.setTalkClose(isTalkWhenClose(UID));
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

	@Override
	public List<Node> getNodes() {
		return null;
	}
}