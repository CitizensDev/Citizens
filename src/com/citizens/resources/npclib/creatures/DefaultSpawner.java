package com.citizens.resources.npclib.creatures;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.citizens.Constants;
import com.citizens.events.NPCCreatureSpawnEvent;
import com.citizens.properties.properties.UtilityProperties;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.npclib.NPCSpawner;

public class DefaultSpawner implements Spawner {
	private final Random random = new Random(System.currentTimeMillis());

	@Override
	public HumanNPC spawn(CreatureNPCType type, Location loc) {
		int offsetX = 0, offsetZ = 0, offset = 25;
		offsetX += offset * getRandomInt(random, 2);
		offsetZ += offset * getRandomInt(random, 2);

		int startX = loc.getBlockX() + offsetX;
		int startZ = loc.getBlockZ() + offsetZ;
		int searchY = 3, searchXZ = 4;

		World world = loc.getWorld();
		for (int y = loc.getBlockY() + searchY; y >= loc.getBlockY() - searchY; --y) {
			for (int x = startX - searchXZ; x <= startX + searchXZ; ++x) {
				for (int z = startZ - searchXZ; z <= startZ + searchXZ; ++z) {
					if (type.spawnOn().isValid(
							world.getBlockTypeIdAt(x, y - 1, z))
							& type.spawnIn().isValid(
									world.getBlockTypeIdAt(x, y, z))
							&& type.spawnIn().isValid(
									world.getBlockTypeIdAt(x, y + 1, z))) {
						if (world.isChunkLoaded(world.getChunkAt(x, z))) {
							if (areEntitiesOnBlock(world.getChunkAt(x, z), x,
									y, z)) {
								if (canSpawn(type)) {
									HumanNPC npc = NPCSpawner.spawnNPC(0,
											UtilityProperties
													.getRandomName(type), loc
													.getWorld(), x, y, z,
											random.nextInt(360), 0, type);
									// call NPC creature-spawning event
									NPCCreatureSpawnEvent spawnEvent = new NPCCreatureSpawnEvent(
											npc, loc);
									Bukkit.getServer().getPluginManager()
											.callEvent(spawnEvent);
									if (spawnEvent.isCancelled()) {
										return null;
									}
									return npc;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	private int getRandomInt(Random random, int max) {
		int ran = random.nextInt(max);
		return random.nextBoolean() ? -ran : ran;
	}

	private boolean canSpawn(CreatureNPCType type) {
		boolean spawn = false;
		switch (type) {
		case EVIL:
			spawn = Constants.spawnEvils;
			break;
		case PIRATE:
			spawn = Constants.spawnPirates;
			break;
		}
		return spawn;
	}

	private boolean areEntitiesOnBlock(Chunk chunk, int x, int y, int z) {
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

}
