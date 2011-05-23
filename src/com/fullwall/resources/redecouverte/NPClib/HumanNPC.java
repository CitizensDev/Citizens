package com.fullwall.resources.redecouverte.NPClib;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.Citizens.NPCTypes.Bandits.BanditNPC;
import com.fullwall.Citizens.NPCTypes.Blacksmiths.BlacksmithNPC;
import com.fullwall.Citizens.NPCTypes.Guards.GuardNPC;
import com.fullwall.Citizens.NPCTypes.Healers.HealerNPC;
import com.fullwall.Citizens.NPCTypes.Questers.QuesterNPC;
import com.fullwall.Citizens.NPCTypes.Traders.TraderNPC;
import com.fullwall.Citizens.NPCTypes.Wizards.WizardNPC;
import com.fullwall.Citizens.NPCs.NPCData;

public class HumanNPC extends NPC {

	private CraftNPC mcEntity;

	private double balance;
	private boolean isTrader = false;
	private boolean isHealer = false;
	private boolean isWizard = false;
	private boolean isBlacksmith = false;
	private boolean isQuester = false;
	private boolean isBandit = false;
	private boolean isGuard = false;

	private TraderNPC traderNPC = new TraderNPC(this);
	private HealerNPC healerNPC = new HealerNPC(this);
	private WizardNPC wizardNPC = new WizardNPC(this);
	private BlacksmithNPC blacksmithNPC = new BlacksmithNPC(this);
	private QuesterNPC questerNPC = new QuesterNPC(this);
	private BanditNPC banditNPC = new BanditNPC(this);
	private GuardNPC guardNPC = new GuardNPC(this);
	private NPCData npcdata;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("Minecraft");

	public HumanNPC(CraftNPC entity, int UID, String name) {
		super(UID, name);
		this.mcEntity = entity;
		this.mcEntity.npc = this;
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

	public BlacksmithNPC getBlacksmith() {
		return this.blacksmithNPC;
	}

	public QuesterNPC getQuester() {
		return this.questerNPC;
	}

	public BanditNPC getBandit() {
		return this.banditNPC;
	}

	public GuardNPC getGuard() {
		return this.guardNPC;
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

	public void setBandit(boolean enable) {
		this.isBandit = enable;
	}

	public boolean isBandit() {
		return this.isBandit;
	}

	public void setGuard(boolean enable) {
		this.isGuard = enable;
	}

	public boolean isGuard() {
		return this.isGuard;
	}

	// For Teleportation
	public void moveTo(double x, double y, double z, float yaw, float pitch) {
		this.mcEntity.setLocation(x, y, z, yaw, pitch);
	}

	public void moveTo(Location loc) {
		this.mcEntity.setLocation(loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), loc.getPitch());
	}

	// For NPC movement
	public void moveNPC(double x, double y, double z) {
		this.mcEntity.setPosition(x, y, z);
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
		this.moveNPCTowardsTarget();
		this.applyGravity();
	}

	private void applyGravity() {
		this.mcEntity.applyGravity();
	}

	private void moveNPCTowardsTarget() {
		this.mcEntity.updateMove();
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

	public boolean createPath(Location loc, int pathTicks, int stationaryTicks,
			float range) {
		return this.mcEntity.startPath(loc, pathTicks, stationaryTicks, range);
	}

	public boolean pathFinished() {
		return mcEntity.pathFinished();
	}

	public void cancelPath() {
		mcEntity.cancelPath();
	}

	public void cancelTarget() {
		mcEntity.cancelTarget();
	}

	public void targetPlayer(LivingEntity entity, boolean aggro, int pathTicks,
			int stationaryTicks, float range) {
		this.mcEntity.setTarget(entity, aggro, pathTicks, stationaryTicks,
				range);
	}
}