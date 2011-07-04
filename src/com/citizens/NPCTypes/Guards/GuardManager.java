package com.citizens.NPCTypes.Guards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;

import com.citizens.Constants;
import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Utils.PathUtils;

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
	public static void addToBlacklist(GuardNPC guard, String mob) {
		List<String> blacklist = guard.getBlacklist();
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
	public static void removeFromBlacklist(GuardNPC guard, String mob) {
		List<String> blacklist = guard.getBlacklist();
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
	 * Add a player to a guard's whitelist
	 * 
	 * @guard
	 * @param player
	 */
	public static void addToWhitelist(GuardNPC guard, String player) {
		if (player.equalsIgnoreCase("all")) {
			List<String> emptyList = new ArrayList<String>();
			emptyList.add("all");
			guard.setWhitelist(emptyList);
			return;
		}
		guard.getWhitelist().add(player);
	}

	/**
	 * Remove a player from a guard's whitelist
	 * 
	 * @param guard
	 * @param player
	 */
	public static void removeFromWhitelist(GuardNPC guard, String player) {
		List<String> whitelist = guard.getWhitelist();
		if (player.equalsIgnoreCase("all")) {
			for (String name : whitelist) {
				whitelist.remove(name);
			}
			return;
		}
		whitelist.remove(player);
	}

	/**
	 * Return a bouncer to its original position
	 * 
	 * @param npc
	 */
	public static void returnToBase(HumanNPC npc) {
		Location loc;
		if (npc.getWaypoints().size() > 0) {
			loc = npc.getWaypoints().current();
		} else {
			loc = npc.getNPCData().getLocation();
		}
		PathUtils.createPath(npc, loc, -1, -1, Constants.pathFindingRange);
	}
}