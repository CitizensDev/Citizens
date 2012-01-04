package net.citizensnpcs.lib.creatures;

import java.util.Random;

import net.citizensnpcs.lib.CraftNPC;
import net.citizensnpcs.lib.NPCSpawner;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class DefaultSpawner implements Spawner {
	private final Random random = new Random(System.currentTimeMillis());

	private DefaultSpawner() {
	}

	private boolean areEntitiesOnBlock(Chunk chunk, int x, int y, int z) {
		for (Entity entity : chunk.getEntities()) {
			if (!(entity instanceof LivingEntity))
				continue;
			Location loc = entity.getLocation();
			if (loc.getBlockX() == x && loc.getBlockY() == y
					&& loc.getBlockZ() == z) {
				return true;
			}
		}
		return false;
	}

	private int getRandomInt(int max) {
		if (max == 0)
			max = 1;
		int ran = random.nextInt(Math.abs(max));
		return random.nextBoolean() ? -ran : ran;
	}

	@Override
	public CraftNPC spawn(CreatureNPCType type, Location loc) {
		if (!type.shouldSpawn())
			return null;
		// TODO: Make this more random.
		int offset = 25 * getRandomInt(1);
		int offsetX = loc.getBlockX() + offset, offsetZ = loc.getBlockZ()
				+ offset;
		if (offset < 0) {
			offsetX -= getRandomInt(offset);
			offsetZ -= getRandomInt(offset);
		} else {
			offsetX += getRandomInt(offset);
			offsetZ += getRandomInt(offset);
		}
		int searchY = 3, searchXZ = 4, shiftedX = 0, shiftedZ = 0;

		World world = loc.getWorld();
		for (int y = loc.getBlockY() + searchY; y >= loc.getBlockY() - searchY; --y) {
			for (int x = offsetX - searchXZ; x <= offsetX + searchXZ; ++x) {
				for (int z = offsetZ - searchXZ; z <= offsetZ + searchXZ; ++z) {
					shiftedX = x >> 4;
					shiftedZ = z >> 4;
					if (!world.isChunkLoaded(shiftedX, shiftedZ)
							|| !type.validSpawnPosition(world, x, y, z)
							|| !type.isSpawn())
						continue;
					if (areEntitiesOnBlock(
							world.getChunkAt(shiftedX, shiftedZ), x, y, z))
						continue;
					CraftNPC npc = NPCSpawner.spawnNPC(
							new Location(loc.getWorld(), x, y, z, random
									.nextInt(360), 0), type);
					return npc;
				}
			}
		}
		return null;
	}

	public static final DefaultSpawner INSTANCE = new DefaultSpawner();
}