package com.fullwall.Citizens;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCSpawner;
import com.fullwall.resources.redecouverte.NPClib.Creatures.CreatureNPC;
import com.fullwall.resources.redecouverte.NPClib.Creatures.CreatureNPCType;

public class CreatureTask implements Runnable {
	public final static Map<Integer, CreatureNPC> creatureNPCs = new ConcurrentHashMap<Integer, CreatureNPC>();
	private final static EnumMap<CreatureNPCType, Integer> spawned = new EnumMap<CreatureNPCType, Integer>(
			CreatureNPCType.class);
	private Player[] online;
	private static boolean dirty = true;

	@Override
	public void run() {
		if (dirty) {
			online = Bukkit.getServer().getOnlinePlayers();
			dirty = false;
		}
		if (online != null && online.length > 0) {
			Player player = online[new Random().nextInt(online.length)];
			// TODO - work out best method of getting creature type to spawn
			// (perhaps randomly?).
			CreatureNPCType type = CreatureNPCType.values()[new Random(
					System.currentTimeMillis()).nextInt(CreatureNPCType
					.values().length)];
			if (spawned.get(type) == null) {
				spawned.put(type, 0);
			} else if (spawned.get(type) <= type.getMaxSpawnable() - 1) {
				HumanNPC npc = spawnCreature(type, player.getLocation());
				if (npc != null) {
					spawned.put(type, spawned.get(type) + 1);
					creatureNPCs.put(npc.getPlayer().getEntityId(),
							(CreatureNPC) npc.getHandle());
					onSpawn(creatureNPCs.get(npc.getPlayer().getEntityId()));
				}
			}
		}
	}

	private void onSpawn(CreatureNPC creatureNPC) {
		creatureNPC.onSpawn();
	}

	private int getRandomInt(Random random, int max) {
		int ran = random.nextInt(max);
		return random.nextBoolean() ? -ran : ran;
	}

	private HumanNPC spawnCreature(CreatureNPCType type, Location loc) {
		Random random = new Random(System.currentTimeMillis());
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
									return NPCSpawner.spawnBasicHumanNpc(0,
											UtilityProperties
													.getRandomName(type), loc
													.getWorld(), x, y, z,
											random.nextInt(360), 0, type);
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	private boolean canSpawn(CreatureNPCType type) {
		boolean spawn = false;
		switch (type) {
		case EVIL:
			spawn = Constants.spawnEvils;
		case PIRATE:
			spawn = Constants.spawnPirates;
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

	public static void setDirty() {
		dirty = true;
	}

	public static void despawnAll() {
		for (CreatureNPC creature : creatureNPCs.values()) {
			NPCSpawner.removeBasicHumanNpc(creature.npc);
		}
	}

	public static void despawn(CreatureNPC npc) {
		removeFromMaps(npc);
		NPCSpawner.removeBasicHumanNpc(npc.npc);
	}

	public static void removeFromMaps(CreatureNPC npc) {
		creatureNPCs.remove(npc.getBukkitEntity().getEntityId());
		spawned.put(npc.getType(), spawned.get(npc.getType()) - 1);
	}

	public static void onDamage(Entity entity, EntityDamageEvent event) {
		if (getCreature(entity) != null) {
			creatureNPCs.get(entity.getEntityId()).onDamage(event);
		}
	}

	public static void onEntityDeath(Entity entity) {
		if (getCreature(entity) != null) {
			CreatureNPC creatureNPC = creatureNPCs.get(entity.getEntityId());
			creatureNPC.onDeath();
			removeFromMaps(creatureNPC);
			NPCSpawner.removeBasicHumanNpc(creatureNPC.npc);
		}
	}

	public static CreatureNPC getCreature(Entity entity) {
		return creatureNPCs.get(entity.getEntityId());
	}

	public static class CreatureTick implements Runnable {
		@Override
		public void run() {
			for (CreatureNPC npc : creatureNPCs.values()) {
				NPCSpawner.removeNPCFromPlayerList(npc.npc);
				npc.doTick();
			}
		}
	}
}