package com.fullwall.Citizens.NPCTypes.Bandits;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BanditInterface {

	/**
	 * Handles what happens when a bandit gets right clicked.
	 * 
	 * @param npc
	 * @param player
	 */
	public static void handleRightClick(HumanNPC npc, Player player) {
		if (NPCManager.validateOwnership(player, npc.getUID())) {
			NPCManager.showInventory(npc, player);
		} else {
			player.sendMessage(ChatColor.RED
					+ "You cannot loot from a bandit that is not yours.");
		}
	}
}