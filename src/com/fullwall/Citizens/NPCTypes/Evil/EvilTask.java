package com.fullwall.Citizens.NPCTypes.Evil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCSpawner;

public class EvilTask implements Runnable {
	public final static List<HumanNPC> evilNPCs = new ArrayList<HumanNPC>();
	private final Integer[] weapons = { 261, 267, 268, 272, 276, 283 };
	private static int heldItem = 261;

	@Override
	public void run() {
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		if (online.length > 0) {
			Player player = online[new Random().nextInt(online.length)];
			if (evilNPCs.size() <= Constants.maxEvilNPCs - 1) {
				HumanNPC npc = spawnEvil(player.getLocation());
				if (npc != null) {
					npc.getInventory().setItemInHand(
							new ItemStack(weapons[new Random()
									.nextInt(weapons.length)], 1));
					heldItem = npc.getInventory().getItemInHand().getTypeId();
					npc.setEvil(true);
					npc.getHandle().setRandomPather(true);
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
		int searchY = 3, searchXZ = 4;
		World world = loc.getWorld();
		for (int y = loc.getBlockY() + searchY; y <= loc.getBlockY() - searchY; --y) {
			for (int x = startX - searchXZ; x <= startX + searchXZ; ++x) {
				for (int z = startZ - searchXZ; z <= startZ + searchXZ; ++z) {
					if (world.getBlockTypeIdAt(x, y - 1, z) != 0
							&& world.getBlockTypeIdAt(x, y, z) == 0
							&& world.getBlockTypeIdAt(x, y + 1, z) == 0) {
						if (world.isChunkLoaded(world.getChunkAt(x, z))) {
							if (spaceEntityFree(world.getChunkAt(x, z), x, y, z)) {
								return NPCSpawner.spawnBasicHumanNpc(0,
										UtilityProperties.getRandomName(),
										loc.getWorld(), x, y, z,
										random.nextInt(360), 0);
							}
						}
					}
				}
			}
		}
		return null;
	}

	private boolean spaceEntityFree(Chunk chunk, int x, int y, int z) {
		for (Entity entity : chunk.getEntities()) {
			if (entity instanceof LivingEntity) {
				Location loc = entity.getLocation();
				if (loc.getBlockX() == x && loc.getBlockY() == y
						&& loc.getBlockZ() == z) {
					return false;
				}
			}
		}
		return true;
	}

	public static void despawnAll() {
		for (HumanNPC evil : evilNPCs) {
			NPCSpawner.removeBasicHumanNpc(evil);
		}
	}

	public static void despawn(Entity entity) {
		int count = 0;
		boolean found = false;
		for (HumanNPC evil : evilNPCs) {
			if (evil.getPlayer().getEntityId() == entity.getEntityId()) {
				NPCSpawner.removeBasicHumanNpc(evil);
				found = true;
			} else {
				++count;
			}
		}
		if (found) {
			evilNPCs.remove(count);
			dropEvilLoot(entity);
		}
	}

	private static void dropEvilLoot(Entity entity) {
		entity.getWorld().dropItemNaturally(entity.getLocation(),
				new ItemStack(Material.getMaterial(heldItem), 1));
		Random random = new Random();
		List<Integer> itemIDs = new ArrayList<Integer>();
		for (int id = 0; id < 400; id++) {
			if (Material.getMaterial(id) != null) {
				itemIDs.add(id);
			}
		}
		Material randomItem = Material.getMaterial(random.nextInt(itemIDs
				.size()));
		if (randomItem == null) {
			return;
		}
		entity.getWorld().dropItemNaturally(entity.getLocation(),
				new ItemStack(randomItem, random.nextInt(2)));
	}

	public static HumanNPC getEvil(Entity entity) {
		for (HumanNPC npc : evilNPCs) {
			if (npc.getPlayer().getEntityId() == entity.getEntityId()) {
				return npc;
			}
		}
		return null;
	}

	public static class EvilTick implements Runnable {
		@Override
		public void run() {
			for (HumanNPC npc : evilNPCs) {
				if (!npc.getEvil().isTame()) {
					// TODO: better range? changes to pathing?
					if (!npc.getHandle().hasTarget()
							&& npc.getHandle().findClosestPlayer(25) != null) {
						npc.getHandle().targetClosestPlayer(true, 25);
					} else {
						npc.getHandle().updateMove();
					}
				}
			}
		}
	}
}