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
	private GuardType guardType = GuardType.NULL;
	private List<String> mobBlacklist = new ArrayList<String>();
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
	 * Get whether or not a guard NPC is a bodyguard
	 * 
	 * @return
	 */
	public boolean isBodyguard() {
		return isBodyguard;
	}

	/**
	 * Set whether or not a guard NPC is a bodyguard
	 * 
	 * @param state
	 */
	public void setBodyguard(boolean state) {
		this.isBodyguard = state;
	}

	/**
	 * Get whether or not a guard NPC is a bouncer
	 * 
	 * @return
	 */
	public boolean isBouncer() {
		return isBouncer;
	}

	/**
	 * Set whether or not a guard NPC is a bouncer
	 * 
	 * @param state
	 */
	public void setBouncer(boolean state) {
		this.isBouncer = state;
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
					mobBlacklist.add(type.toString().toLowerCase()
							.replace("_", " "));
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
}