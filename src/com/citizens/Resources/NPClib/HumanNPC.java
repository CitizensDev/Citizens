package com.citizens.Resources.NPClib;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.PlayerInventory;

import com.citizens.Interfaces.Clickable;
import com.citizens.Interfaces.Damageable;
import com.citizens.Interfaces.NPCFactory;
import com.citizens.Interfaces.Targetable;
import com.citizens.Interfaces.Toggleable;
import com.citizens.NPCs.NPCData;
import com.citizens.Properties.PropertyManager;
import com.citizens.Resources.NPClib.NPCAnimator.Animation;

public class HumanNPC extends NPC {
	private CraftNPC mcEntity;
	private NPCData npcdata;
	private double balance;
	private boolean paused;
	private final WaypointPath waypoints = new WaypointPath();

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
		this.getPlayer().teleport(loc);
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

	public boolean isPaused() {
		return this.paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void callLeftClick(Player player, HumanNPC npc) {
		for (Toggleable t : types.values()) {
			if (t instanceof Clickable) {
				((Clickable) t).onLeftClick(player, npc);
			}
		}
	}

	public void callRightClick(Player player, HumanNPC npc) {
		for (Toggleable t : types.values()) {
			if (t instanceof Clickable) {
				((Clickable) t).onRightClick(player, npc);
			}
		}
	}

	public Collection<Toggleable> types() {
		return this.types.values();
	}

	public boolean callDamageEvent(EntityDamageEvent event) {
		boolean found = false;
		for (Toggleable t : types.values()) {
			if (t instanceof Damageable) {
				((Damageable) t).onDamage(event);
				if (!found)
					found = true;
			}
		}
		return found;
	}

	public boolean callTargetEvent(EntityTargetEvent event) {
		boolean found = false;
		for (Toggleable t : types.values()) {
			if (t instanceof Targetable) {
				((Targetable) t).onTarget(event);
				if (!found)
					found = true;
			}
		}
		return found;
	}
}