package net.citizensnpcs.guards;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.TickTask;
import net.citizensnpcs.guards.GuardManager.GuardState;
import net.citizensnpcs.guards.flags.FlagList;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.PathUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

public class Guard extends CitizensNPC {
	private boolean isAggressive = true;
	private boolean isAttacking = false;
	private boolean returning = false;
	private GuardState guardState = GuardState.NULL;
	private final FlagList flags = new FlagList();
	private double radius = 10;

	// Get whether a guard NPC is a bodyguard
	public boolean isBodyguard() {
		return guardState == GuardState.BODYGUARD;
	}

	// Set whether a guard NPC is a bodyguard
	public void setBodyguard() {
		guardState = GuardState.BODYGUARD;
	}

	// Set whether a guard NPC is a bodyguard
	public boolean isBouncer() {
		return guardState == GuardState.BOUNCER;
	}

	// Set whether a guard NPC is a bouncer
	public void setBouncer() {
		guardState = GuardState.BOUNCER;
	}

	public void clear() {
		guardState = GuardState.NULL;
	}

	// Get whether a bodyguard NPC kills on sight
	public boolean isAggressive() {
		return isAggressive;
	}

	// Set whether a bodyguard kills on sight
	public void setAggressive(boolean state) {
		this.isAggressive = state;
	}

	// Get the type of guard that a guard NPC is
	public GuardState getGuardState() {
		return guardState;
	}

	// Set the type of a guard that a guard NPC is
	public void setGuardState(GuardState guardState) {
		this.guardState = guardState;
	}

	// Get a guard's blacklist
	public FlagList getFlags() {
		return flags;
	}

	// Get the protection radius for a bouncer
	public double getProtectionRadius() {
		return radius;
	}

	// Get the halved protection radius for a bouncer
	public double getHalvedProtectionRadius() {
		return this.radius / 2;
	}

	// Set the protection radius for a bouncer
	public void setProtectionRadius(double radius) {
		this.radius = radius;
	}

	public void setAttacking(boolean attacking) {
		this.isAttacking = attacking;
	}

	public boolean isAttacking() {
		return isAttacking;
	}

	@Override
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof HumanNPC)) {
			return;
		}
		HumanNPC npc = (HumanNPC) event.getEntity();
		if (isAggressive() && event.getCause() == DamageCause.ENTITY_ATTACK) {
			EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
			if (!isOwner(ev.getDamager(), npc)) {
				target((LivingEntity) ev.getDamager(), npc);
			}
		}
	}

	private boolean isOwner(Entity damager, HumanNPC npc) {
		return damager instanceof Player ? NPCManager.validateOwnership(
				(Player) damager, npc.getUID()) : false;
	}

	@Override
	public void onDeath(EntityDeathEvent event) {
		HumanNPC npc = NPCManager.get(event.getEntity());
		Player player = Bukkit.getServer().getPlayer(npc.getOwner());
		if (player != null) {
			player.sendMessage(ChatColor.GRAY + "Your guard NPC "
					+ StringUtils.wrap(npc.getStrippedName(), ChatColor.GRAY)
					+ " died.");
		}
		event.getDrops().clear();
		TickTask.scheduleRespawn(npc,
				SettingsManager.getInt("GuardRespawnDelay"));
	}

	public void target(LivingEntity entity, HumanNPC npc) {
		npc.setPaused(true);
		this.setAttacking(true);
		PathUtils.target(npc, entity, true, -1, -1,
				SettingsManager.getDouble("PathfindingRange"));
	}

	@Override
	public CitizensNPCType getType() {
		return new GuardType();
	}

	public void setReturning(boolean returning) {
		this.returning = returning;
	}

	public boolean isReturning() {
		return returning;
	}
}