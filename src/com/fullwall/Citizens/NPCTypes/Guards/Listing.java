package com.fullwall.Citizens.NPCTypes.Guards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Enums.GuardListType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class Listing {
	private List<String> list;
	private GuardListType type;
	private boolean add = true;
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
		add = true;
		modifyList(player, npc, toAdd);
	}

	/**
	 * Remove a string from the list
	 * 
	 * @param player
	 * @param npc
	 * @param toRemove
	 */
	public void remove(Player player, HumanNPC npc, String toRemove) {
		add = false;
		modifyList(player, npc, toRemove);
	}

	/**
	 * Change all values from a list
	 * 
	 * @return
	 */
	private void changeAll() {
		for (CreatureType type : CreatureType.values()) {
			if (add) {
				if (!list.contains(type.toString().toLowerCase()
						.replace("_", " "))) {
					list.add(type.toString().toLowerCase().replace("_", " "));
				}
			} else {
				if (list.contains(type.toString().toLowerCase()
						.replace("_", " "))) {
					list.remove(type.toString().toLowerCase().replace("_", " "));
				}
			}
		}
	}

	/**
	 * Modify a list
	 * 
	 * @param player
	 * @param npc
	 * @param toChange
	 */
	private void modifyList(Player player, HumanNPC npc, String toChange) {
		String msg = ChatColor.YELLOW + npc.getStrippedName();
		if (add) {
			msg += ChatColor.GREEN + " has added ";
			if (type == GuardListType.BLACK) {
				if (toChange.equalsIgnoreCase("all")) {
					changeable = true;
					changeAll();
					msg += ChatColor.GREEN + "all mobs ";
				} else if (validateMob(player, toChange)) {
					changeable = true;
					msg += ChatColor.YELLOW + toChange + " ";
				}
			} else if (type == GuardListType.WHITE) {
				changeable = true;
				msg += toChange + " ";
			} else {
				changeable = false;
			}
		} else {
			msg += ChatColor.GREEN + " has removed ";
			if (type == GuardListType.BLACK) {
				if (toChange.equalsIgnoreCase("all")) {
					changeable = true;
					changeAll();
					msg += ChatColor.GREEN + "all mobs ";
				} else if (validateMob(player, toChange)) {
					changeable = true;
					msg += ChatColor.YELLOW + toChange + " ";
				}
			} else if (type == GuardListType.WHITE) {
				changeable = true;
				msg += toChange + " ";
			} else {
				changeable = false;
			}
		}
		if (!toChange.equalsIgnoreCase("all") && changeable) {
			if (!list.contains(toChange) && add) {
				list.add(toChange);
			} else if (list.contains(toChange) && !add) {
				list.remove(toChange);
			}
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
}