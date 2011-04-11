package com.fullwall.Citizens.Traders;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderInterface {
	public static ArrayList<Integer> tasks = new ArrayList<Integer>();

	public enum Mode {
		NORMAL, STOCK, INFINITE
	}

	public static void showInventory(HumanNPC NPC, Player player) {
		((CraftPlayer) player).getHandle()
				.a(NPC.getMinecraftEntity().inventory);
	}

	public static void handleRightClick(HumanNPC NPC, Player player) {
		if (NPC.isFree()) {
			if (!NPCManager.validateOwnership(NPC.getUID(), player)) {
				TraderTask task = new TraderTask(NPC, player, Citizens.plugin,
						Mode.NORMAL);
				int id = Citizens.plugin.getServer().getScheduler()
						.scheduleSyncRepeatingTask(Citizens.plugin, task, 0, 1);
				tasks.add(id);
				task.addID(id);
				NPC.setFree(false);
			} else {
				TraderTask task = new TraderTask(NPC, player, Citizens.plugin,
						Mode.STOCK);
				int id = Citizens.plugin.getServer().getScheduler()
						.scheduleSyncRepeatingTask(Citizens.plugin, task, 0, 1);
				tasks.add(id);
				task.addID(id);
				NPC.setFree(false);
			}
			showInventory(NPC, player);
		} else
			player.sendMessage(ChatColor.RED
					+ "Only one person may be served at a time!");
	}
}
