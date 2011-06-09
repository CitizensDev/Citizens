package com.fullwall.Citizens.NPCTypes.Evil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCSpawner;

public class EvilTask implements Runnable {

	private List<HumanNPC> evilNPCs = new ArrayList<HumanNPC>();

	@Override
	public void run() {
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		Player player = online[new Random().nextInt(online.length)];
		if (evilNPCs.size() <= Constants.maxEvilNPCs - 1) {
			HumanNPC npc = NPCSpawner.spawnBasicHumanNpc(0, UtilityProperties
					.getRandomName(), player.getWorld(), player.getLocation()
					.getBlockX() + 10, player.getLocation().getBlockY(), player
					.getLocation().getBlockZ() + 15, player.getLocation()
					.getYaw(), player.getLocation().getPitch());
			Integer[] swords = { 267, 268, 272, 276, 283 };
			npc.getInventory().setItemInHand(
					new ItemStack(swords[new Random().nextInt(swords.length)],
							1));
			npc.setEvil(true);
			evilNPCs.add(npc);
		}
		for (HumanNPC hnpc : evilNPCs) {
			if (!hnpc.getEvil().isTame()) {
				// TODO pathing
				// Example: npc.getMinecraftEntity().takeRandomPath();
				// Example:
				// npc.getMinecraftEntity().targetClosestPlayer(aggro,
				// range);
				// Example:
				// npc.getMinecraftEntity().findClosestPlayer(range);
			}
		}
	}
}