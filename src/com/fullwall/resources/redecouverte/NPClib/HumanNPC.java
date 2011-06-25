package com.fullwall.resources.redecouverte.NPClib;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.Citizens.Interfaces.NPCFactory;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.NPCs.NPCData;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.NPCAnimator.Animation;

public class HumanNPC extends NPC {
	private CraftNPC mcEntity;
	private double balance;
	private NPCData npcdata;
	private boolean paused;
	private WaypointPath waypoints = new WaypointPath();

	private final ConcurrentHashMap<String, Toggleable> types = new ConcurrentHashMap<String, Toggleable>();

	public HumanNPC(CraftNPC entity, int UID, String name) {
		super(UID, name);
		this.mcEntity = entity;
		this.mcEntity.npc = this;
	}

	public <T extends Toggleable> void addType(String type, NPCFactory factory) {
		registerType(type, factory);
		PropertyManager.load(type, this);
	}

	public void removeType(String type) {
		this.types.remove(type);
		PropertyManager.save(type, this);
	}

	public boolean isType(String type) {
		return this.types.get(type) != null;
	}

	public void registerType(String type, NPCFactory factory) {
		Toggleable t = factory.create(this);
		this.types.put(type, t);
	}

	@SuppressWarnings("unchecked")
	public <T> T getToggleable(String type) {
		return (T) this.types.get(type);
	}

	public WaypointPath getWaypoints() {
		return this.waypoints;
	}

	public Player getPlayer() {
		return (Player) this.mcEntity.getBukkitEntity();
	}

	public CraftNPC getHandle() {
		return this.mcEntity;
	}

	public Chunk getChunk() {
		return this.getLocation().getBlock().getChunk();
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
		this.mcEntity.setLocation(loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), loc.getPitch());
	}

	public void updateMovement() {
		this.mcEntity.updateMove();
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
		return getPlayer().getLocation();
	}

	public int getHealth() {
		return getPlayer().getHealth();
	}

	public void setHealth(int health) {
		getPlayer().setHealth(health);
	}

	public NPCData getNPCData() {
		return npcdata;
	}

	public PlayerInventory getInventory() {
		return getPlayer().getInventory();
	}

	public World getWorld() {
		return getPlayer().getWorld();
	}

	public double getBalance() {
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public boolean createPath(Location loc, int pathTicks, int stationaryTicks,
			float range) {
		return this.mcEntity.startPath(loc, pathTicks, stationaryTicks, range);
	}

	public void target(LivingEntity entity, boolean aggro, int pathTicks,
			int stationaryTicks, float range) {
		this.mcEntity.setTarget(entity, aggro, pathTicks, stationaryTicks,
				range);
	}

	public void setAttackTimes(int times) {
		this.mcEntity.setAttackTimes(times);
	}

	public boolean paused() {
		return this.paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void callLeftClick(Player player, HumanNPC npc) {
		for (Toggleable t : types.values()) {
			if (Clickable.class.isAssignableFrom(t.getClass())) {
				((Clickable) t).onLeftClick(player, npc);
			}
		}
	}

	public void callRightClick(Player player, HumanNPC npc) {
		for (Toggleable t : types.values()) {
			if (Clickable.class.isAssignableFrom(t.getClass())) {
				((Clickable) t).onRightClick(player, npc);
			}
		}
	}

	public Collection<Toggleable> types() {
		return this.types.values();
	}

	public void setWaypoints(WaypointPath waypoints) {
		this.waypoints = waypoints;
	}
}