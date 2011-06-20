package com.fullwall.Citizens.NPCTypes.Guards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Misc.Enums.GuardType;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class GuardNPC implements Toggleable, Clickable {
	private final HumanNPC npc;
	private boolean isBodyguard = true;
	private boolean isBouncer = false;
	private boolean isAggressive = false;
	private GuardType guardType = GuardType.BODYGUARD;
	private List<String> blacklist = new ArrayList<String>();
	private List<String> whitelist = new ArrayList<String>();
	private double radius = 10;

	/**
	 * Guard NPC object
	 * 
	 * @param npc
	 */
	public GuardNPC(HumanNPC npc) {
		this.npc = npc;
	}

	/**
	 * Get whether a guard NPC is a bodyguard
	 * 
	 * @return
	 */
	public boolean isBodyguard() {
		return isBodyguard;
	}

	/**
	 * Set whether a guard NPC is a bodyguard
	 * 
	 * @param state
	 */
	public void setBodyguard(boolean state) {
		this.isBodyguard = state;
	}

	/**
	 * Get whether a guard NPC is a bouncer
	 * 
	 * @return
	 */
	public boolean isBouncer() {
		return isBouncer;
	}

	/**
	 * Set whether a guard NPC is a bouncer
	 * 
	 * @param state
	 */
	public void setBouncer(boolean state) {
		this.isBouncer = state;
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
		if (isBodyguard()) {
			guardType = GuardType.BODYGUARD;
		} else if (isBouncer()) {
			guardType = GuardType.BOUNCER;
		} else {
			guardType = GuardType.NULL;
		}
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
	 * Set the protection radius for a bouncer
	 * 
	 * @param radius
	 */
	public void setProtectionRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public void toggle() {
		npc.setGuard(!npc.isGuard());
	}

	@Override
	public boolean getToggle() {
		return npc.isGuard();
	}

	@Override
	public String getName() {
		return npc.getStrippedName();
	}

	@Override
	public String getType() {
		return "guard";
	}

	@Override
	public void saveState() {
		PropertyManager.get(getType()).saveState(npc);
	}

	@Override
	public void register() {
		PropertyManager.get(getType()).register(npc);
	}

	public double getHalvedRadius() {
		return this.radius / 2;
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
	}
}