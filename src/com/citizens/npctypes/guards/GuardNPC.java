package com.citizens.npctypes.guards;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import com.citizens.Constants;
import com.citizens.TickTask;
import com.citizens.npcs.NPCManager;
import com.citizens.npctypes.guards.GuardManager.GuardType;
import com.citizens.npctypes.interfaces.Clickable;
import com.citizens.npctypes.interfaces.Damageable;
import com.citizens.npctypes.interfaces.Targetable;
import com.citizens.npctypes.interfaces.Toggleable;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.PathUtils;
import com.citizens.utils.StringUtils;

public class GuardNPC extends Toggleable implements Clickable, Damageable,
		Targetable {
	private boolean isAggressive = false;
	private boolean isAttacking = false;
	private GuardType guardType = GuardType.NULL;
	private Set<String> blacklist = new HashSet<String>();
	private Set<String> whitelist = new HashSet<String>();
	private double radius = 10;

	/**
	 * Guard NPC object
	 * 
	 * @param npc
	 */
	public GuardNPC(HumanNPC npc) {
		super(npc);
	}

	/**
	 * Get whether a guard NPC is a bodyguard
	 * 
	 * @return
	 */
	public boolean isBodyguard() {
		return guardType == GuardType.BODYGUARD;
	}

	/**
	 * Set whether a guard NPC is a bodyguard
	 * 
	 * @param state
	 */
	public void setBodyguard() {
		guardType = GuardType.BODYGUARD;
	}

	/**
	 * Get whether a guard NPC is a bouncer
	 * 
	 * @return
	 */
	public boolean isBouncer() {
		return guardType == GuardType.BOUNCER;
	}

	/**
	 * Set whether a guard NPC is a bouncer
	 * 
	 * @param state
	 */
	public void setBouncer() {
		guardType = GuardType.BOUNCER;
	}

	public void clear() {
		guardType = GuardType.NULL;
	}

	/**
	 * Get whether a bodyguard NPC kills on sight
	 * 
	 * @return
	 */

	public boolean isAggressive() {
		return isAggressive;
	}

	/**
	 * Set whether a bodyguard kills on sight
	 * 
	 * @param state
	 */
	public void setAggressive(boolean state) {
		this.isAggressive = state;
	}

	/**
	 * Get the type of guard that a guard NPC is
	 * 
	 * @return
	 */
	public GuardType getGuardType() {
		return guardType;
	}

	/**
	 * Set the type of a guard that a guard NPC is
	 * 
	 * @param guardType
	 */
	public void setGuardType(GuardType guardType) {
		this.guardType = guardType;
	}

	/**
	 * Get a guard's blacklist
	 * 
	 * @return
	 */
	public Set<String> getBlacklist() {
		return blacklist;
	}

	/**
	 * Set the list of unallowed mobs for a bodyguard's zone
	 * 
	 * @param mobBlacklist
	 */
	public void setBlacklist(Set<String> blacklist) {
		this.blacklist = blacklist;
	}

	public boolean isBlacklisted(String name) {
		return this.blacklist.contains("all") || this.blacklist.contains(name);
	}

	/**
	 * Get a list of players allowed to enter a bodyguard's zone
	 * 
	 * @return
	 */
	public Set<String> getWhitelist() {
		return whitelist;
	}

	/**
	 * Set the list of allowed players in a bodyguard's zone
	 * 
	 * @param whitelist
	 */
	public void setWhitelist(Set<String> whitelist) {
		this.whitelist = whitelist;
	}

	public boolean isWhiteListed(Player player) {
		return isOwner(player) || this.whitelist.contains("all")
				|| this.whitelist.contains(player.getName());
	}

	/**
	 * Get the protection radius for a bouncer
	 * 
	 * @return
	 */
	public double getProtectionRadius() {
		return radius;
	}

	/**
	 * Get the halved protection radius for a bouncer
	 * 
	 * @return
	 */
	public double getHalvedProtectionRadius() {
		return this.radius / 2;
	}

	/**
	 * Set the protection radius for a bouncer
	 * 
	 * @param radius
	 */
	public void setProtectionRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public String getType() {
		return "guard";
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
	}

	public void setAttacking(boolean attacking) {
		this.isAttacking = attacking;
	}

	public boolean isAttacking() {
		return isAttacking;
	}

	@Override
	public void onTarget(EntityTargetEvent event) {
	}

	@Override
	public void onDamage(EntityDamageEvent event) {
		if (this.npc.getPlayer().getHealth() - event.getDamage() <= 0) {
			return;
		}
		if (isAggressive() && event.getCause() == DamageCause.ENTITY_ATTACK) {
			EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
			if (!isOwner(ev.getDamager()))
				target((LivingEntity) ev.getDamager());
		}
	}

	private boolean isOwner(Entity damager) {
		return damager instanceof Player ? NPCManager.validateOwnership(
				(Player) damager, npc.getUID(), false) : false;
	}

	@Override
	public void onDeath(EntityDeathEvent event) {
		Player player = Bukkit.getServer().getPlayer(npc.getOwner());
		if (player != null)
			player.sendMessage(ChatColor.GRAY + "Your guard NPC "
					+ StringUtils.wrap(npc.getStrippedName(), ChatColor.GRAY)
					+ " died.");
		event.getDrops().clear();
		TickTask.scheduleRespawn(this.npc, Constants.guardRespawnDelay);
	}

	public void target(LivingEntity entity) {
		this.npc.setPaused(true);
		this.setAttacking(true);
		PathUtils.target(npc, entity, true, -1, -1, Constants.pathFindingRange);
	}
}