package com.fullwall.resources.redecouverte.NPClib;

import java.util.logging.Logger;

import net.minecraft.server.Packet7UseEntity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.Citizens.Blacksmiths.BlacksmithNPC;
import com.fullwall.Citizens.Healers.HealerNPC;
import com.fullwall.Citizens.NPCs.NPCData;
import com.fullwall.Citizens.Questers.QuesterNPC;
import com.fullwall.Citizens.Traders.TraderNPC;
import com.fullwall.Citizens.Wizards.WizardNPC;

public class HumanNPC extends NPC {

	private CraftNPC mcEntity;
	private double fallingSpeed = 0.0;
	private double GravityPerSecond = 9.81;
	private double movementSpeed = 0.2;
	private double privateSpace = 1.5;

	private double balance;

	private double targetX = 0.0;
	@SuppressWarnings("unused")
	private double targetY = 0.0;
	private double targetZ = 0.0;
	private Player targetPlayer = null;
	private boolean hasTarget = false;
	private boolean isTrader = false;
	private boolean isHealer = false;
	private boolean isWizard = false;
	private boolean isQuester = false;
	private boolean isBlacksmith = false;

	private TraderNPC traderNPC = new TraderNPC(this);
	private HealerNPC healerNPC = new HealerNPC(this);
	private WizardNPC wizardNPC = new WizardNPC(this);
	private QuesterNPC questerNPC = new QuesterNPC(this);
	private BlacksmithNPC blacksmithNPC = new BlacksmithNPC(this);
	private NPCData npcdata;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("Minecraft");

	public HumanNPC(CraftNPC entity, int UID, String name) {
		super(UID, name);
		this.mcEntity = entity;
	}

	public Player getPlayer() {
		return (Player) this.mcEntity.getBukkitEntity();
	}

	public CraftNPC getMinecraftEntity() {
		return this.mcEntity;
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

	public QuesterNPC getQuester() {
		return this.questerNPC;
	}

	public BlacksmithNPC getBlacksmith() {
		return this.blacksmithNPC;
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

	public void setQuester(boolean enable) {
		this.isQuester = enable;
	}

	public boolean isQuester() {
		return this.isQuester;
	}

	public void setBlacksmith(boolean enable) {
		this.isBlacksmith = enable;
	}

	public boolean isBlacksmith() {
		return this.isBlacksmith;
	}

	// For Teleportation
	public void moveTo(double x, double y, double z, float yaw, float pitch) {
		this.mcEntity.setPositionRotation(x, y, z, yaw, pitch);
	}

	public void moveTo(Location loc) {
		this.mcEntity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), loc.getPitch());
	}

	// For NPC movement
	public void moveNPC(double x, double y, double z) {
		this.mcEntity.setPosition(x, y, z);
	}

	public void setTarget(double x, double y, double z) {
		this.targetX = x;
		this.targetY = y;
		this.targetZ = z;
		this.hasTarget = true;
		this.targetPlayer = null;
	}

	public void setTarget(Player p) {
		this.targetPlayer = p;
		this.hasTarget = false;
	}

	public void moveNPCTowardsTarget() {
		double tX = targetX;
		double tZ = targetZ;
		if (targetPlayer != null) {
			tX = targetPlayer.getLocation().getX();
			tZ = targetPlayer.getLocation().getZ();
		}
		double xDiff = tX - this.mcEntity.locX;
		double zDiff = tZ - this.mcEntity.locZ;
		double length = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		xDiff = (xDiff / length) * movementSpeed;
		zDiff = (zDiff / length) * movementSpeed;
		if (length > privateSpace) {
			double prevX = this.mcEntity.locX;
			double prevZ = this.mcEntity.locZ;
			this.moveNPC(xDiff, 0, zDiff);
			double xDiff2 = this.mcEntity.locX - prevX;
			double zDiff2 = this.mcEntity.locZ - prevZ;
			if (Math.abs(xDiff2 - xDiff) > 0.01
					|| Math.abs(zDiff2 - zDiff) > 0.01) {
				this.jumpNPC();
			}
		} else {
			this.hasTarget = false;
		}
	}

	public void removeTarget() {
		this.hasTarget = false;
		this.targetPlayer = null;
	}

	public double getX() {
		return this.mcEntity.locX;
	}

	public double getY() {
		return this.mcEntity.locY;
	}

	public double getZ() {
		return this.mcEntity.locZ;
	}

	public void updateMovement() {
		if (this.hasTarget == true || this.targetPlayer != null)
			this.moveNPCTowardsTarget();
		// this.applyGravity();
	}

	public void jumpNPC() {
		if (fallingSpeed == 0.0)
			fallingSpeed = 0.59;
	}

	public void applyGravity() {
		fallingSpeed -= GravityPerSecond / 100;
		double prevY = this.mcEntity.locY;
		this.mcEntity.f(0, fallingSpeed, 0);
		double diff = this.mcEntity.locY - prevY;
		if (diff - fallingSpeed > 0.01)
			fallingSpeed = 0.0;
	}

	public void attackLivingEntity(LivingEntity ent) {
		try {
			this.mcEntity.animateArmSwing();
			/*
			 * Field f = CraftLivingEntity.class.getDeclaredField("entity");
			 * f.setAccessible(true); EntityLiving lEntity = (EntityLiving)
			 * f.get(ent); this.mcEntity.h(lEntity); Probably have to find the
			 * minecraft code that simply attacks an entity (so as to include
			 * item damage bonuses) ent.damage(1);
			 */
			// I think this is right (may need testing).
			Packet7UseEntity packet = new Packet7UseEntity();
			packet.k = true;
			packet.target = ent.getEntityId();
			this.mcEntity.netServerHandler.sendPacket(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void animateArmSwing() {
		this.mcEntity.animateArmSwing();
	}

	public void actHurt() {
		this.mcEntity.actHurt();
	}

	public void crouch() {
		this.mcEntity.crouch();
	}

	public void uncrouch() {
		this.mcEntity.uncrouch();
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

	public NPCData getNPCData() {
		return npcdata;
	}

	public PlayerInventory getInventory() {
		return getPlayer().getInventory();
	}

	public double getBalance() {
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}