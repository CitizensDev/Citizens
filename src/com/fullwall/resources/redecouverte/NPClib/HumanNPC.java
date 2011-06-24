package com.fullwall.resources.redecouverte.NPClib;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.NPCTypes.Blacksmiths.BlacksmithNPC;
import com.fullwall.Citizens.NPCTypes.Evils.EvilNPC;
import com.fullwall.Citizens.NPCTypes.Guards.GuardNPC;
import com.fullwall.Citizens.NPCTypes.Healers.HealerNPC;
import com.fullwall.Citizens.NPCTypes.Pirates.PirateNPC;
import com.fullwall.Citizens.NPCTypes.Questers.QuesterNPC;
import com.fullwall.Citizens.NPCTypes.Traders.TraderNPC;
import com.fullwall.Citizens.NPCTypes.Wizards.WizardNPC;
import com.fullwall.Citizens.NPCs.NPCData;
import com.fullwall.resources.redecouverte.NPClib.NPCAnimator.Action;

public class HumanNPC extends NPC {
	private CraftNPC mcEntity;
	private double balance;

	private boolean isTrader = false;
	private boolean isHealer = false;
	private boolean isWizard = false;
	private boolean isBlacksmith = false;
	private boolean isQuester = false;
	private boolean isGuard = false;
	private boolean isEvil = true;
	private boolean isPirate = true;

	private final TraderNPC traderNPC = new TraderNPC(this);
	private final HealerNPC healerNPC = new HealerNPC(this);
	private final WizardNPC wizardNPC = new WizardNPC(this);
	private final BlacksmithNPC blacksmithNPC = new BlacksmithNPC(this);
	private final QuesterNPC questerNPC = new QuesterNPC(this);
	private final GuardNPC guardNPC = new GuardNPC(this);
	private final EvilNPC evilNPC = new EvilNPC(this);
	private final PirateNPC pirateNPC = new PirateNPC(this);

	private List<Location> waypoints = new ArrayList<Location>();
	private boolean waypointStarted = false;
	private int waypointIndex = 0;

	private NPCData npcdata;

	public HumanNPC(CraftNPC entity, int UID, String name) {
		super(UID, name);
		this.mcEntity = entity;
		this.mcEntity.npc = this;
	}

	public Location getWaypoint(int index) {
		return this.getWaypoints().get(index);
	}

	public void resetWaypoints() {
		setWaypoints(new ArrayList<Location>());
		this.waypointIndex = 0;
	}

	public void addWaypoint(Location location) {
		this.getWaypoints().add(location);
	}

	public void removeLastWaypoint() {
		this.getWaypoints().remove(this.getWaypoints().size() - 1);
	}

	public int getWaypointSize() {
		return getWaypoints().size();
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

	public TraderNPC getTrader() {
		return this.traderNPC;
	}

	public HealerNPC getHealer() {
		return this.healerNPC;
	}

	public WizardNPC getWizard() {
		return this.wizardNPC;
	}

	public BlacksmithNPC getBlacksmith() {
		return this.blacksmithNPC;
	}

	public QuesterNPC getQuester() {
		return this.questerNPC;
	}

	public GuardNPC getGuard() {
		return this.guardNPC;
	}

	public EvilNPC getEvil() {
		return this.evilNPC;
	}

	public PirateNPC getPirate() {
		return this.pirateNPC;
	}

	protected CraftNPC getMCEntity() {
		return this.mcEntity;
	}

	public void setTrader(boolean enable) {
		this.isTrader = enable;
	}

	public boolean isTrader() {
		return this.isTrader;
	}

	public void setHealer(boolean enable) {
		this.isHealer = enable;
	}

	public boolean isHealer() {
		return this.isHealer;
	}

	public void setWizard(boolean enable) {
		this.isWizard = enable;
	}

	public boolean isWizard() {
		return this.isWizard;
	}

	public void setBlacksmith(boolean enable) {
		this.isBlacksmith = enable;
	}

	public boolean isBlacksmith() {
		return this.isBlacksmith;
	}

	public void setQuester(boolean enable) {
		this.isQuester = enable;
	}

	public boolean isQuester() {
		return this.isQuester;
	}

	public void setGuard(boolean enable) {
		this.isGuard = enable;
	}

	public boolean isGuard() {
		return this.isGuard;
	}

	public void setEvil(boolean enable) {
		this.isEvil = enable;
	}

	public boolean isEvil() {
		return this.isEvil;
	}

	public void setPirate(boolean enable) {
		this.isPirate = enable;
	}

	public boolean isPirate() {
		return this.isPirate;
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

	public void performAction(Action action) {
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

	public Toggleable getType(String type) {
		if (type.equals("blacksmith")) {
			return this.getBlacksmith();
		} else if (type.equals("guard")) {
			return this.getGuard();
		} else if (type.equals("healer")) {
			return this.getHealer();
		} else if (type.equals("quester")) {
			return this.getQuester();
		} else if (type.equals("trader")) {
			return this.getTrader();
		} else if (type.equals("wizard")) {
			return this.getWizard();
		}
		return null;
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

	public void setWaypointIndex(int waypointIndex) {
		this.waypointIndex = waypointIndex;
	}

	public int getWaypointIndex() {
		return waypointIndex;
	}

	public void setWaypointStarted(boolean waypointStarted) {
		this.waypointStarted = waypointStarted;
	}

	public boolean isWaypointStarted() {
		return waypointStarted;
	}

	public List<Location> getWaypoints() {
		return this.waypoints;
	}

	public void setWaypoints(List<Location> waypoints) {
		this.waypoints = waypoints;
	}
}