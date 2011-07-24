package com.citizens.npctypes.guards;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;

import com.citizens.SettingsManager.Constant;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.PathUtils;

public class GuardManager {
	public enum GuardType {
		/**
		 * Protects land
		 */
		BOUNCER,
		/**
		 * Protects and follows players
		 */
		BODYGUARD,
		/**
		 * Default guard type
		 */
		NULL;

		public static GuardType parse(String string) {
			try {
				return GuardType.valueOf(string.toUpperCase());
			} catch (Exception ex) {
				return NULL;
			}
		}
	}

	/**
	 * Add a mob to guard's blacklist
	 * 
	 * @param guard
	 * @param mob
	 */
	public static void addToBlacklist(Guard guard, String mob) {
		Set<String> blacklist = guard.getBlacklist();
		if (mob.equalsIgnoreCase("all")) {
			for (CreatureType type : CreatureType.values()) {
				if (!blacklist.contains(type.toString().toLowerCase())) {
					blacklist.add(type.toString().toLowerCase()
							.replace("_", ""));
				}
			}
			return;
		}
		blacklist.add(mob.toLowerCase());
	}

	/**
	 * Remove a mob from a guard's blacklist
	 * 
	 * @param guard
	 * @param mob
	 */
	public static void removeFromBlacklist(Guard guard, String mob) {
		Set<String> blacklist = guard.getBlacklist();
		if (mob.equalsIgnoreCase("all")) {
			for (CreatureType type : CreatureType.values()) {
				if (blacklist.contains(type.toString().toLowerCase())) {
					blacklist.remove(type.toString().toLowerCase()
							.replace("_", ""));
				}
			}
			return;
		}
		blacklist.remove(mob.toLowerCase());
	}

	/**
	 * Return a bouncer to its original position
	 * 
	 * @param npc
	 */
	public static void returnToBase(HumanNPC npc) {
		Location loc;
		if (npc.getWaypoints().size() > 0) {
			loc = npc.getWaypoints().current().getLocation();
		} else {
			loc = npc.getNPCData().getLocation();
		}
		PathUtils.createPath(npc, loc, -1, -1,
				Constant.PathfindingRange.toDouble());
	}
}