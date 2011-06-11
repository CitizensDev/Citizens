package com.fullwall.Citizens.NPCTypes.Evil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCSpawner;

public class EvilTask implements Runnable {
	private final static List<HumanNPC> evilNPCs = new ArrayList<HumanNPC>();
	private final Integer[] weapons = { 261, 267, 268, 272, 276, 283 };

	@Override
	public void run() {
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		if (online.length > 0) {
			Player player = online[new Random().nextInt(online.length)];
			if (evilNPCs.size() <= Constants.maxEvilNPCs - 1) {
				// TODO: better spawning code
				HumanNPC npc = spawnEvil(player.getLocation());
				if (npc != null) {
					npc.getInventory().setItemInHand(
							new ItemStack(weapons[new Random()
									.nextInt(weapons.length)], 1));
					npc.setEvil(true);
					evilNPCs.add(npc);
				}
			}
		}
	}

	private HumanNPC spawnEvil(Location loc) {
		Random random = new Random(System.currentTimeMillis());
		int offsetX = 0, offsetZ = 0;
		int offset = 25;
		switch (random.nextInt(7)) {
		case 0:
			offsetX = offset;
		case 1:
			offsetZ = offset;
		case 2:
			offsetX = -offset;
		case 3:
			offsetZ = -offset;
		case 4:
			offsetX = offset;
			offsetZ = offset;
		case 5:
			offsetX = -offset;
			offsetZ = offset;
		case 6:
			offsetX = offset;
			offsetZ = -offset;
		}
		int startX = loc.getBlockX() + offsetX;
		int startZ = loc.getBlockZ() + offsetZ;
		int searchY = 1, searchXZ = 4;
		World world = loc.getWorld();
		for (int y = loc.getBlockY() - searchY; y <= loc.getBlockY() + searchY; ++y) {
			for (int x = startX - searchXZ; x <= startX + searchXZ; ++x) {
				for (int z = startZ - searchXZ; z <= startZ + searchXZ; ++z) {
					if (world.getBlockTypeIdAt(x, y, z) == 0
							&& world.getBlockTypeIdAt(x, y, z) == 0) {
						// TODO: check if entity is already there.
						return NPCSpawner
								.spawnBasicHumanNpc(0,
										UtilityProperties.getRandomName(),
										loc.getWorld(), x, y, z,
										random.nextInt(360), 0);
					}
				}
			}
		}
		return null;
	}

	public static void despawnAll() {
		for (HumanNPC evil : evilNPCs) {
			NPCSpawner.removeBasicHumanNpc(evil);
		}
	}

	public static class EvilTick implements Runnable {
		@Override
		public void run() {
			for (HumanNPC evil : evilNPCs) {
				if (!evil.getEvil().isTame()) {
					// TODO: better range? changes to pathing?
					if (evil.getHandle().findClosestPlayer(25) == null) {
						evil.getHandle().takeRandomPath();
					} else {
						evil.getHandle().targetClosestPlayer(true, 25);
					}
				}
			}
		}
	}
}