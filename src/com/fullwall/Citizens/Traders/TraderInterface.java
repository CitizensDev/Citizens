package com.fullwall.Citizens.Traders;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
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
	 * Uses craftbukkit methods to show a player an npc's inventory screen.
	 * 
	 * @param npc
	 * @param player
	 */
	public static void showInventory(HumanNPC npc, Player player) {
		((CraftPlayer) player).getHandle()
				.a(npc.getMinecraftEntity().inventory);
	}

	/**
	 * Handles what happens when an npc gets right clicked.
	 * 
	 * @param npc
	 * @param player
	 */
	public static void handleRightClick(HumanNPC npc, Player player) {
		if (npc.getTrader().isFree()) {
			if (!Permission.hasPermission("citizens.trader.stock", player)) {
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
					.scheduleSyncRepeatingTask(Citizens.plugin, task, 0, 1);
			tasks.add(id);
			task.addID(id);
			npc.getTrader().setFree(false);
			showInventory(npc, player);
		} else
			player.sendMessage(ChatColor.RED
					+ "Only one person may be served at a time!");
	}
}