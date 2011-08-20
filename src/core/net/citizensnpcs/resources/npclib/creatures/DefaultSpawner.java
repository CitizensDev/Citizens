package net.citizensnpcs.resources.npclib.creatures;

import java.util.Random;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.events.NPCSpawnEvent;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCSpawner;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class DefaultSpawner implements Spawner {
	public static final DefaultSpawner INSTANCE = new DefaultSpawner();

	private DefaultSpawner() {

	}

	private final Random random = new Random(System.currentTimeMillis());

	@Override
	public HumanNPC spawn(CreatureNPCType type, Location loc) {
		if (random.nextInt(100) < type.getSpawnChance())
			return null;
		// TODO: Make this more random.
		int offset = 25 * getRandomInt(random, 1);
		int offsetX = loc.getBlockX() + offset, offsetZ = loc.getBlockZ()
				+ offset;
		if (offset < 0) {
			offsetX -= getRandomInt(random, offset);
			offsetZ -= getRandomInt(random, offset);
		} else {
			offsetX += getRandomInt(random, offset);
			offsetZ += getRandomInt(random, offset);
		}
		int searchY = 3, searchXZ = 4, shiftedX = 0, shiftedZ = 0;

		World world = loc.getWorld();
		Chunk last = null;
		for (int y = loc.getBlockY() + searchY; y >= loc.getBlockY() - searchY; --y) {
			for (int x = offsetX - searchXZ; x <= offsetX + searchXZ; ++x) {
				for (int z = offsetZ - searchXZ; z <= offsetZ + searchXZ; ++z) {
					shiftedX = x >> 4;
					shiftedZ = z >> 4;
					if (world.isChunkLoaded(shiftedX, shiftedZ)) {
						if (world.getChunkAt(shiftedX, shiftedZ) != last)
							last = world.getChunkAt(shiftedX, shiftedZ);
						if (type.spawnOn().isValid(
								world.getBlockTypeIdAt(x, y - 1, z))
								& type.spawnIn().isValid(
										world.getBlockTypeIdAt(x, y, z))
								&& type.spawnIn().isValid(
										world.getBlockTypeIdAt(x, y + 1, z))) {
							if (areEntitiesOnBlock(last, x, y, z)) {
								if (canSpawn(type)) {
									HumanNPC npc = NPCSpawner.spawnNPC(-1,
											UtilityProperties
													.getRandomName(type), loc
													.getWorld(), x, y, z,
											random.nextInt(360), 0, type);
									// call NPC creature-spawning event
									NPCSpawnEvent spawnEvent = new NPCSpawnEvent(
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
			spawn = SettingsManager.getBoolean("SpawnEvils");
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