package net.citizensnpcs.lib;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.Settings;
import net.citizensnpcs.economy.Account;
import net.citizensnpcs.lib.NPCAnimator.Animation;
import net.citizensnpcs.lib.pathfinding.PathController;
import net.citizensnpcs.npcdata.ItemData;
import net.citizensnpcs.npcdata.NPCData;
import net.citizensnpcs.npcdata.NPCDataManager;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.waypoints.LinearWaypointPath;
import net.citizensnpcs.waypoints.WaypointPath;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
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
	private final WaypointPath waypoints;

	private final Map<String, CitizensNPC> types = new MapMaker().makeMap();

	private boolean spawned = false;

	private HumanNPC(int UID) {
		super(UID, getRoot(UID).getString("basic.name"));
		String[] location = getRoot().getRelative("basic")
				.getString("location").split(",");
		if (location.length == 6) {
			npcdata.setLocation(new Location(Bukkit.getServer().getWorld(
					location[0]), Double.parseDouble(location[1]), Double
					.parseDouble(location[2]), Double.parseDouble(location[3]),
					Float.parseFloat(location[4]), Float
							.parseFloat(location[5])));
			tryCreateMCEntity();
		}
		waypoints = mcEntity == null ? null : new LinearWaypointPath(this);
	}

	private HumanNPC(int UID, String name, Location location) {
		super(UID, name);
		this.npcdata.setLocation(location);
		tryCreateMCEntity();
		waypoints = new LinearWaypointPath(this);
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
		if (!spawned)
			return;
		this.mcEntity.world.removeEntity(this.mcEntity);
		spawned = false;
	}

	public void doTick() {
		this.mcEntity.tick();
	}

	public Account getAccount() {
		return this.account;
	}

	public Location getBaseLocation() {
		return this.npcdata.getLocation();
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

	public PathController getPathController() {
		return this.mcEntity.getPathController();
	}

	public Player getPlayer() {
		return (Player) this.mcEntity.getBukkitEntity();
	}

	private DataKey getRoot() {
		return getRoot(this.getUID());
	}

	@SuppressWarnings("unchecked")
	public <T> T getType(String type) {
		return (T) this.types.get(type);
	}

	public WaypointPath getWaypoints() {
		return this.waypoints;
	}

	public World getWorld() {
		return this.getPlayer().getWorld();
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
		if (root.keyExists("owner"))
			npcdata.setOwner(root.getString("owner"));
		npcdata.setTexts(new ArrayDeque<String>(Arrays.asList(root.getString(
				"text").split(";"))));
		npcdata.setColour(ChatColor.getByCode(Integer.parseInt(root.getString(
				"color", "15"))));
		account.set(root.getDouble("balance", 0D));

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
		waypoints.load(root.getRelative("waypoints"));
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
		instance.save(root);
		root.setBoolean("toggle", false);
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
		waypoints.save(root);
	}

	public void setItemInHand(ItemStack item) {
		this.getPlayer().setItemInHand(item);
	}

	@Override
	public void setName(String changeTo) {
		super.setName(changeTo);
		this.mcEntity.name = changeTo;
		despawn();
		spawn();
	}

	public void setNPCData(NPCData npcdata) {
		this.npcdata = npcdata;
	}

	public void spawn() {
		if (spawned)
			return;
		spawned = true;
		this.mcEntity.name = this.npcdata.getColour() == ChatColor.WHITE ? this
				.getName() : this.npcdata.getColour() + this.getName();
		((CraftWorld) this.getPlayer().getLocation().getWorld()).getHandle()
				.addEntity(this.mcEntity);
		this.mcEntity.world.players.remove(this.mcEntity);
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
			this.mcEntity = NPCSpawner.createNPC(name,
					this.npcdata.getLocation());
			if (mcEntity == null)
				return false;
		}
		return true;
	}

	public Collection<CitizensNPC> types() {
		return this.types.values();
	}

	public static HumanNPC createAndLoad(int UID) {
		HumanNPC npc = new HumanNPC(UID);
		if (npc.getNPCData().getLocation() == null || npc.getHandle() == null)
			return null;
		npc.load();
		return npc;
	}

	public static HumanNPC createAndLoad(int UID, String name,
			Location location) {
		HumanNPC npc = new HumanNPC(UID, name, location);
		npc.load();
		return npc;
	}

	private static DataKey getRoot(int UID) {
		return PropertyManager.getNPCProfiles().getKey(Integer.toString(UID));
	}
}