package com.fullwall.Citizens.NPCTypes.Guards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Enums.GuardListType;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class Listing {
	private List<String> list;
	private GuardListType type;
	private boolean changeable = false;

	/**
	 * Create a list for a guard
	 * 
	 * @param type
	 */
	public Listing(GuardListType type) {
		list = new ArrayList<String>();
		this.type = type;
	}

	/**
	 * Add a string to the list
	 * 
	 * @param toAdd
	 */
	public void add(Player player, HumanNPC npc, String toAdd) {
		String msg = StringUtils.wrap(npc.getStrippedName()) + " has added ";
		toAdd = toAdd.toLowerCase();
		if (type == GuardListType.BLACK) {
			if (toAdd.equalsIgnoreCase("all")) {
				changeable = true;
				addAll();
				msg += ChatColor.GREEN + "all mobs ";
			} else if (validateMob(player, toAdd)) {
				changeable = true;
				msg += ChatColor.YELLOW + toAdd + " ";
			}
		} else if (type == GuardListType.WHITE) {
			changeable = true;
			msg += toAdd + " ";
		} else {
			changeable = false;
		}
		if (!list.contains(toAdd) && changeable) {
			if (!toAdd.equalsIgnoreCase("all")) {
				list.add(toAdd);
			}
			msg += ChatColor.GREEN + "to it's " + type.toString().toLowerCase()
					+ "list.";
			player.sendMessage(msg);
		}
	}

	/**
	 * Remove a string from the list
	 * 
	 * @param player
	 * @param npc
	 * @param toRemove
	 */
	public void remove(Player player, HumanNPC npc, String toRemove) {
		String msg = StringUtils.wrap(npc.getStrippedName()) + " has removed ";
		toRemove = toRemove.toLowerCase();
		if (type == GuardListType.BLACK) {
			if (toRemove.equalsIgnoreCase("all")) {
				changeable = true;
				removeAll();
				msg += ChatColor.GREEN + "all mobs ";
			} else if (validateMob(player, toRemove)) {
				changeable = true;
				msg += ChatColor.YELLOW + toRemove + " ";
			}
		} else if (type == GuardListType.WHITE) {
			changeable = true;
			msg += toRemove + " ";
		} else {
			changeable = false;
		}
		if (!list.contains(toRemove) && changeable) {
			if (!toRemove.equalsIgnoreCase("all")) {
				list.remove(toRemove);
			}
			msg += ChatColor.GREEN + "from it's "
					+ type.toString().toLowerCase() + "list.";
			player.sendMessage(msg);
		}
	}

	/**
	 * Check if a mob is a valid type
	 * 
	 * @param player
	 * @param mob
	 * @return
	 */
	private boolean validateMob(Player player, String mob) {
		if (CreatureType.fromName(mob.replaceFirst("" + mob.charAt(0), ""
				+ Character.toUpperCase(mob.charAt(0)))) != null) {
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "Not a valid mob.");
			return false;
		}
	}

	/**
	 * Add all mobs to a list
	 * 
	 * @return
	 */
	private void addAll() {
		if (type == GuardListType.BLACK) {
			for (CreatureType type : CreatureType.values()) {
				if (!list.contains(type.toString().toLowerCase()
						.replace("_", " "))) {
					list.add(type.toString().toLowerCase().replace("_", " "));
				}
			}
		}
	}

	/**
	 * Remove all mobs from a list
	 * 
	 * @return
	 */
	private void removeAll() {
		if (type == GuardListType.BLACK) {
			for (CreatureType type : CreatureType.values()) {
				if (list.contains(type.toString().toLowerCase()
						.replace("_", " "))) {
					list.remove(type.toString().toLowerCase().replace("_", " "));
				}
			}
		}
	}
}