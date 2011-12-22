package net.citizensnpcs.lib;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.Settings;
import net.citizensnpcs.economy.Account;
import net.citizensnpcs.lib.NPCAnimator.Animation;
import net.citizensnpcs.npcdata.ItemData;
import net.citizensnpcs.npcdata.NPCData;
import net.citizensnpcs.npcdata.NPCDataManager;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.waypoints.Waypoint;
import net.citizensnpcs.waypoints.WaypointModifier;
import net.citizensnpcs.waypoints.WaypointModifierType;
import net.citizensnpcs.waypoints.WaypointPath;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;

public class HumanNPC extends NPC<Player> {
	private CraftNPC mcEntity;
	private NPCData npcdata = new NPCData();
	private final Account account = new Account() {
		private double balance;

		@Override
		public void add(double amount) {
			balance += amount;
		}

		@Override
		public double balance() {
			return balance;
		}

		@Override
		public boolean hasEnough(double amount) {
			return balance >= amount;
		}

		@Override
		public void set(double balance) {
			this.balance = balance;
		}

		@Override
		public void subtract(double amount) {
			balance -= amount;
		}
	};
	private boolean paused;
	private WaypointPath waypoints = new WaypointPath();

	private final Map<String, CitizensNPC> types = new MapMaker().makeMap();

	public HumanNPC(int UID) {
		super(UID, getRoot(UID).getString("basic.name"));
	}

	public HumanNPC(int UID, String name) {
		super(UID, name);
	}

	public <T extends CitizensNPC> void addType(String type) {
		if (!NPCTypeManager.validType(type))
			throw new IllegalArgumentException("illegal type: " + type
					+ " added to: " + this.getUID());
		CitizensNPC instance = NPCTypeManager.getType(type).newInstance();
		types.put(type, instance);
		DataKey root = getRoot().getRelative(instance.getType().getName());
		root.setBoolean("toggle", true);
		instance.load(root);
	}

	public void callDamageEvent(EntityDamageEvent event) {
		if (types.size() == 0) {
			event.setCancelled(true);
			return;
		}
		for (CitizensNPC type : types.values()) {
			type.onDamage(event);
		}
	}

	public void callDeathEvent(EntityDeathEvent event) {
		for (CitizensNPC type : types.values()) {
			type.onDeath(event);
		}
	}

	public void callLeftClick(Player player, HumanNPC npc) {
		for (CitizensNPC type : types.values()) {
			type.onLeftClick(player, npc);
		}
	}

	public void callRightClick(Player player, HumanNPC npc) {
		for (CitizensNPC type : types.values()) {
			type.onRightClick(player, npc);
		}
	}

	public void callTargetEvent(EntityTargetEvent event) {
		for (CitizensNPC type : types.values()) {
			type.onTarget(event);
		}
	}

	public void despawn() {
		this.mcEntity.world.removeEntity(this.mcEntity);
	}

	public void doTick() {
		this.mcEntity.moveTick();
		this.mcEntity.applyGravity();
	}

	public Account getAccount() {
		return this.account;
	}

	public Location getBaseLocation() {
		return this.waypoints.getLast() != null ? this.waypoints.getLast()
				.getLocation() : this.npcdata.getLocation();
	}

	public int getChunkX() {
		return this.getLocation().getBlockX() >> 4;
	}

	public int getChunkZ() {
		return this.getLocation().getBlockZ() >> 4;
	}

	public CraftNPC getHandle() {
		return this.mcEntity;
	}

	public PlayerInventory getInventory() {
		return this.getPlayer().getInventory();
	}

	public ItemStack getItemInHand() {
		return this.getPlayer().getItemInHand();
	}

	public Location getLocation() {
		return this.getPlayer().getLocation();
	}

	public NPCData getNPCData() {
		return npcdata;
	}

	public String getOwner() {
		return this.npcdata.getOwner();
	}

	public Player getPlayer() {
		return (Player) this.mcEntity.getBukkitEntity();
	}

	private DataKey getRoot() {
		return getRoot(this.getUID());
	}

	private static DataKey getRoot(int UID) {
		return PropertyManager.getNPCProfiles().getKey(Integer.toString(UID));
	}

	@SuppressWarnings("unchecked")
	public <T> T getType(String type) {
		return (T) this.types.get(type);
	}

	public WaypointPath getWaypoints() {
		if (waypoints == null) {
			this.waypoints = new WaypointPath();
		}
		return this.waypoints;
	}

	public World getWorld() {
		return this.getPlayer().getWorld();
	}

	public boolean isPaused() {
		return this.paused;
	}

	public boolean isType(String type) {
		return this.types.get(type) != null;
	}

	public void load() {
		DataKey root = getRoot();
		this.load(root.getRelative("basic"));
		for (DataKey subKey : root.getSubKeys()) {
			if (!subKey.keyExists("toggle") || !subKey.getBoolean("toggle"))
				continue;
			if (!this.isType(subKey.name())) {
				types.put(subKey.name(), NPCTypeManager.getType(subKey.name())
						.newInstance());
			}
		}
		for (CitizensNPC instance : types.values()) {
			instance.load(root.getRelative(instance.getType().getName()));
		}
	}

	private void load(DataKey root) {
		String[] location = root.getString("location").split(",");
		if (location.length == 6) {
			npcdata.setLocation(new Location(Bukkit.getServer().getWorld(
					location[0]), Double.parseDouble(location[1]), Double
					.parseDouble(location[2]), Double.parseDouble(location[3]),
					Float.parseFloat(location[4]), Float
							.parseFloat(location[5])));
		}
		if (this.npcdata.getLocation() != null)
			tryCreateMCEntity();
		npcdata.setTalk(root.getBoolean("talk"));
		npcdata.setTalkClose(root.getBoolean("talk-when-close",
				Settings.getBoolean("DefaultTalkClose")));
		npcdata.setLookClose(root.getBoolean("look-when-close",
				Settings.getBoolean("DefaultLookAt")));
		npcdata.setOwner(root.getString("owner"));
		npcdata.setTexts(new ArrayDeque<String>(Arrays.asList(root.getString(
				"text").split(";"))));
		npcdata.setColour(ChatColor.getByCode(Integer.parseInt(root.getString(
				"color", "15"))));
		account.set(root.getDouble("balance"));

		List<ItemData> items = Lists.newArrayList();
		String current = root.getString("items", "0:0,0:0,0:0,0:0,0:0,");
		for (String s : current.split(",")) {
			if (!s.contains(":")) {
				s += ":0";
			}
			String[] parts = s.split(":");
			items.add(new ItemData(Integer.parseInt(parts[0]), Short
					.parseShort(parts[1])));
		}
		npcdata.setItems(items);
		NPCDataManager.addItems(this, npcdata.getItems());

		String save = root.getString("inventory", "AIR*36");
		int i = 0;
		for (String s : save.split(",")) {
			String[] split = s.split("/");
			if (!s.contains("AIR") && !split[0].equals("0")) {
				this.getInventory().setItem(
						i++,
						new ItemStack(StringUtils.parse(split[0]), StringUtils
								.parse(split[1]), (short) StringUtils
								.parse(split[2])));
				continue;
			}
			int count = Integer.parseInt(s.split("\\*")[1]);
			while (count != 0) {
				this.getInventory().setItem(i++, null);
				--count;
			}
		}

		List<Waypoint> points = new ArrayList<Waypoint>();
		for (DataKey key : root.getRelative("waypoints").getIntegerSubKeys()) {
			Waypoint waypoint = new Waypoint(LocationUtils.loadLocation(key,
					true));
			waypoint.setDelay(key.getInt("delay"));

			if (key.keyExists("modifiers")) {
				key = key.getRelative("modifiers");
				for (DataKey innerKey : key.getIntegerSubKeys()) {
					WaypointModifier modifier = WaypointModifierType.valueOf(
							innerKey.getString("type")).create(waypoint);
					modifier.load(innerKey);
					waypoint.addModifier(modifier);
				}
			}
			if (waypoint != null)
				points.add(waypoint);
		}
		waypoints.setPoints(points);
		/*if (values.length != 6) {
					if (values[0].isEmpty()) {
						Messaging.log("Missing location for " + UID, values.length);
					} else
						Messaging.log("Invalid location length. Length: "
								+ values.length);
					return null;
				}*/
	}

	public void performAction(Animation action) {
		this.mcEntity.performAction(action);
	}

	public void removeType(String type) {
		CitizensNPC instance = this.types.remove(type);
		DataKey root = getRoot().getRelative(instance.getType().getName());
		root.setBoolean("toggle", false);
		instance.save(root);
	}

	public void save() {
		DataKey root = getRoot();
		this.save(root.getRelative("basic"));
		for (CitizensNPC instance : types.values()) {
			instance.save(root.getRelative(instance.getType().getName()));
		}
	}

	private void save(DataKey root) {
		root.setString("name", this.getName());
		root.setDouble("balance", account.balance());
		LocationUtils.saveLocation(root, this.npcdata.getLocation(), true);
		root.setInt("color",
				npcdata.getColour() == null ? ChatColor.WHITE.getCode()
						: npcdata.getColour().getCode());
		root.setString("owner", npcdata.getOwner());
		root.setBoolean("talk-when-close", npcdata.isTalkClose());
		root.setBoolean("talk", npcdata.isTalk());
		root.setBoolean("look-when-close", npcdata.isLookClose());
		root.setString("text",
				Joiner.on(";").skipNulls().join(npcdata.getTexts()));
		List<ItemData> items = npcdata.getItems();
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < items.size(); i++) {
			short durability = items.get(i).getDurability();
			temp.append(items.get(i).getID() + ":"
					+ (durability == -1 ? 0 : durability) + ",");
		}
		root.setString("items", temp.toString());
		PlayerInventory inv = this.getInventory();
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
		root.setString("inventory", save.toString());
		List<Waypoint> list = waypoints.getWaypoints();
		if (list.size() == 0)
			return;
		root.removeKey("waypoints"); // clear old
										// waypoints.
		count = 0;
		for (Waypoint waypoint : list) {
			DataKey key = root.getRelative("waypoints." + count);
			LocationUtils.saveLocation(key, waypoint.getLocation(), true);
			key.setInt("delay", waypoint.getDelay());
			key = key.getRelative("modifiers");

			int innercount = 0;
			for (WaypointModifier modifier : waypoint.getModifiers()) {
				DataKey inner = key.getRelative(Integer.toString(innercount));
				inner.setString("type", modifier.getType().name());
				modifier.save(inner);
				++innercount;
			}
			++count;
		}
	}

	public void setItemInHand(ItemStack item) {
		this.getPlayer().setItemInHand(item);
	}

	public void setNPCData(NPCData npcdata) {
		this.npcdata = npcdata;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean spawn() {
		if (!tryCreateMCEntity())
			return false;
		this.mcEntity.name = this.npcdata.getColour() == ChatColor.WHITE ? this
				.getName() : this.npcdata.getColour() + this.getName();
		this.mcEntity.world.addEntity(this.mcEntity);
		this.mcEntity.world.players.remove(this.mcEntity);
		return true;
	}

	public void teleport(Location loc) {
		boolean multiworld = loc.getWorld() != this.getWorld();
		this.getPlayer().teleport(loc);
		if (multiworld) {
			((CraftServer) Bukkit.getServer()).getHandle().players
					.remove(this.mcEntity);
		}
	}

	private boolean tryCreateMCEntity() {
		if (this.mcEntity == null) {
			String name = this.npcdata.getColour() == ChatColor.WHITE ? this
					.getName() : this.npcdata.getColour() + this.getName();
			this.mcEntity = NPCSpawner.createNPC(this.getUID(), name,
					this.npcdata.getLocation());
			if (mcEntity == null)
				return false;
		}
		return true;
	}

	public Collection<CitizensNPC> types() {
		return this.types.values();
	}

	@Override
	public void setName(String changeTo) {
		super.setName(changeTo);
		this.mcEntity.name = changeTo;
		despawn();
		spawn();
	}
}