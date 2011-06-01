package com.fullwall.Citizens.NPCTypes.Traders;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderInterface {
	public static ArrayList<Integer> tasks = new ArrayList<Integer>();

	public enum Mode {
		NORMAL, STOCK, INFINITE
	}

	/**
	 * Handles what happens when a trader gets right clicked.
	 * 
	 * @param npc
	 * @param player
	 */
	public static void handleRightClick(HumanNPC npc, Player player) {
		if (npc.getTrader().isFree()) {
			if (!Permission.canModify(player, "trader")) {
				return;
			}
			Mode mode = Mode.NORMAL;
			if (NPCManager.validateOwnership(player, npc.getUID())) {
				mode = Mode.STOCK;
			} else if (npc.getTrader().isUnlimited()) {
				mode = Mode.INFINITE;
			}
			TraderTask task = new TraderTask(npc, player, Citizens.plugin, mode);
			int id = Bukkit.getServer().getScheduler()
					.scheduleSyncRepeatingTask(Citizens.plugin, task, 0, 0);
			tasks.add(id);
			task.addID(id);
			npc.getTrader().setFree(false);
			NPCManager.showInventory(npc, player);
		} else {
			player.sendMessage(ChatColor.RED
					+ "Only one person may be served at a time!");
		}
	}
}