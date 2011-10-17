package net.citizensnpcs.resources.npclib;

import java.util.Collection;
import java.util.Map;

import net.citizensnpcs.npcdata.NPCData;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.NPCAnimator.Animation;
import net.citizensnpcs.waypoints.WaypointPath;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.common.collect.MapMaker;

public class HumanNPC extends NPC {
	private CraftNPC mcEntity;
	private NPCData npcdata;
	private double balance;
	private boolean paused;
	private WaypointPath waypoints = new WaypointPath();

	private final Map<String, CitizensNPC> types = new MapMaker().makeMap();

	public HumanNPC(CraftNPC entity, int UID, String name) {
		super(UID, name);
		this.mcEntity = entity;
		this.mcEntity.npc = this;
	}

	public <T extends CitizensNPC> void addType(String type) {
		registerType(type);
		PropertyManager.load(type, this);
	}

	public void removeType(String type) {
		this.types.remove(type);
		PropertyManager.save(type, this);
	}

	public boolean isType(String type) {
		return this.types.get(type) != null;
	}

	public void registerType(String type) {
		this.types.put(type, NPCTypeManager.getType(type).getInstance());
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

	public Player getPlayer() {
		return (Player) this.mcEntity.getBukkitEntity();
	}

	public CraftNPC getHandle() {
		return this.mcEntity;
	}

	public void setHandle(CraftNPC handle) {
		this.mcEntity = handle;
	}

	protected CraftNPC getMCEntity() {
		return this.mcEntity;
	}

	public void teleport(double x, double y, double z, float yaw, float pitch) {
		this.mcEntity.setLocation(x, y, z, yaw, pitch);
	}

	public void teleport(Location loc) {
		boolean multiworld = loc.getWorld() != this.getWorld();
		this.getPlayer().teleport(loc);
		if (multiworld) {
			((CraftServer) Bukkit.getServer()).getHandle().players
					.remove(this.mcEntity);
		}
	}

	public void doTick() {
		this.mcEntity.moveTick();
		this.mcEntity.applyGravity();
	}

	public void performAction(Animation action) {
		this.mcEntity.performAction(action);
	}

	public String getOwner() {
		return this.npcdata.getOwner();
	}

	public void setNPCData(NPCData npcdata) {
		this.npcdata = npcdata;
	}

	public Location getLocation() {
		return this.getPlayer().getLocation();
	}

	public int getHealth() {
		return this.getPlayer().getHealth();
	}

	public void setHealth(int health) {
		this.getPlayer().setHealth(health);
	}

	public NPCData getNPCData() {
		return npcdata;
	}

	public PlayerInventory getInventory() {
		return this.getPlayer().getInventory();
	}

	public World getWorld() {
		return this.getPlayer().getWorld();
	}

	public double getBalance() {
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public boolean isPaused() {
		return this.paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
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

	public Collection<CitizensNPC> types() {
		return this.types.values();
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

	public void callTargetEvent(EntityTargetEvent event) {
		for (CitizensNPC type : types.values()) {
			type.onTarget(event);
		}
	}

	public int getChunkX() {
		return this.getLocation().getBlockX() >> 4;
	}

	public int getChunkZ() {
		return this.getLocation().getBlockZ() >> 4;
	}

	public ItemStack getItemInHand() {
		return this.getPlayer().getItemInHand();
	}

	public void setItemInHand(ItemStack item) {
		this.getPlayer().setItemInHand(item);
	}

	public Location getBaseLocation() {
		return this.waypoints.getLast() != null ? this.waypoints.getLast()
				.getLocation() : this.npcdata.getLocation();
	}
}