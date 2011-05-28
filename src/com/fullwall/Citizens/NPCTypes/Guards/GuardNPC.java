package com.fullwall.Citizens.NPCTypes.Guards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.CreatureType;

import com.fullwall.Citizens.Enums.GuardType;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class GuardNPC implements Toggleable {
	private HumanNPC npc;
	private boolean isBodyguard = true;
	private boolean isBouncer = false;
	private boolean killMobs = false;
	private boolean isAggressive = false;
	private boolean killPlayers = false;
	private GuardType guardType = GuardType.NULL;
	private List<String> mobBlacklist = new ArrayList<String>();
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
	 * Get whether a bodyguard NPC kills mobs
	 * 
	 * @return
	 */
	public boolean killMobs() {
		return killMobs;
	}

	/**
	 * Set whether a bodyguard kill mobs
	 * 
	 * @param state
	 */
	public void setkillMobs(boolean state) {
		this.killMobs = state;
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
	public void setisAggressive(boolean state) {
		this.isAggressive = state;
	}

	/**
	 * Get whether a bodyguard NPC kills players
	 * 
	 * @return
	 */
	public boolean killPlayers() {
		return killPlayers;
	}

	/**
	 * Set whether a bodyguard should kill players
	 * 
	 * @param state
	 */
	public void setkillPlayers(boolean state) {
		this.killPlayers = state;
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
	 * Get a list of mobs not allowed entry to a bouncer's zone
	 * 
	 * @return
	 */
	public List<String> getMobBlacklist() {
		for (int x = 0; x < mobBlacklist.size(); x++) {
			mobBlacklist.get(x).split(",");
		}
		return mobBlacklist;
	}

	/**
	 * Add a mob to a list of unallowed mobs for a bouncer's zone
	 * 
	 * @param mob
	 */
	public void addMobToBlacklist(String mob) {
		if (mob.equalsIgnoreCase("all")) {
			for (CreatureType type : CreatureType.values()) {
				if (!mobBlacklist.contains(type.toString().toLowerCase())) {
					mobBlacklist.add(type.toString().toLowerCase().replace("_",
							" "));
				}
			}
		} else {
			mobBlacklist.add(mob.toLowerCase());
		}
	}

	/**
	 * Set the list of unallowed mobs for a bouncer's zone
	 * 
	 * @param mobBlacklist
	 */
	public void setMobBlacklist(List<String> mobBlacklist) {
		this.mobBlacklist = mobBlacklist;
	}

	/**
	 * Get a list of players allowed to enter a bouncer's zone
	 * 
	 * @return
	 */
	public List<String> getWhitelist() {
		for (int x = 0; x < whitelist.size(); x++) {
			whitelist.get(x).split(",");
		}
		return whitelist;
	}

	/**
	 * Add a player to a bouncer's whitelist
	 * 
	 * @param player
	 */
	public void addPlayerToWhitelist(String player) {
		whitelist.add(player.toLowerCase());
	}

	/**
	 * Set the list of allowed players in a bouncer's zone
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
}