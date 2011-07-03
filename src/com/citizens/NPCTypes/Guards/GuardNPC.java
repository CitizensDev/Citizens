package com.citizens.NPCTypes.Guards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;

import com.citizens.Constants;
import com.citizens.TickTask;
import com.citizens.Interfaces.Clickable;
import com.citizens.Interfaces.Damageable;
import com.citizens.Interfaces.Targetable;
import com.citizens.Interfaces.Toggleable;
import com.citizens.NPCTypes.Guards.GuardManager.GuardType;
import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Utils.PathUtils;

public class GuardNPC extends Toggleable implements Clickable, Damageable,
		Targetable {
	private boolean isAggressive = false;
	private boolean isAttacking = false;
	private GuardType guardType = GuardType.NULL;
	private List<String> blacklist = new ArrayList<String>();
	private List<String> whitelist = new ArrayList<String>();
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
	 * Get a list of mobs not allowed entry to a bodyguard's zone
	 * 
	 * @return
	 */
	public List<String> getBlacklist() {
		return blacklist;
	}

	/**
	 * Add a mob to a list of unallowed mobs for a bodyguard's zone
	 * 
	 * @param mob
	 */
	public void addToBlacklist(String mob) {
		if (mob.equalsIgnoreCase("all")) {
			for (CreatureType type : CreatureType.values()) {
				if (!blacklist.contains(type.toString().toLowerCase())) {
					blacklist.add(type.toString().toLowerCase()
							.replace("_", ""));
				}
			}
		} else {
			blacklist.add(mob.toLowerCase());
		}
	}

	/**
	 * Set the list of unallowed mobs for a bodyguard's zone
	 * 
	 * @param mobBlacklist
	 */
	public void setBlacklist(List<String> blacklist) {
		this.blacklist = blacklist;
	}

	/**
	 * Get a list of players allowed to enter a bodyguard's zone
	 * 
	 * @return
	 */
	public List<String> getWhitelist() {
		return whitelist;
	}

	/**
	 * Add a player to a bodyguard's whitelist
	 * 
	 * @param player
	 */
	public void addToWhitelist(String player) {
		whitelist.add(player.toLowerCase());
	}

	/**
	 * Set the list of allowed players in a bodyguard's zone
	 * 
	 * @param whitelist
	 */
	public void setWhitelist(List<String> whitelist) {
		this.whitelist = whitelist;
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
		if (!attacking)
			this.npc.setPaused(false);
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
			TickTask.respawn(this.npc, Constants.guardRespawnDelay);
		}
		if (isAggressive() && event.getCause() == DamageCause.ENTITY_ATTACK) {
			target((LivingEntity) ((EntityDamageByEntityEvent) event)
					.getDamager());
		}
	}

	public void target(LivingEntity entity) {
		this.npc.setPaused(true);
		this.setAttacking(true);
		PathUtils.target(npc, entity, true, -1, -1, Constants.pathFindingRange);
	}

	public void returnToBase() {
		Location loc;
		if (npc.getWaypoints().size() > 0) {
			loc = npc.getWaypoints().current();
		} else {
			loc = npc.getNPCData().getLocation();
		}
		PathUtils.createPath(npc, loc, -1, -1, Constants.pathFindingRange);
	}
}