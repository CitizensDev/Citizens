package com.fullwall.Citizens.NPCTypes.Evil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.Citizens.Utils.Messaging;
import com.fullwall.resources.redecouverte.NPClib.CreatureNPC;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCSpawner;

public class EvilTask implements Runnable {
	public final static Map<Integer, CreatureNPC> creatureNPCs = new HashMap<Integer, CreatureNPC>();
	private final Integer[] weapons = { 261, 267, 268, 272, 276, 283 };

	@Override
	public void run() {
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		if (online.length > 0) {
			Player player = online[new Random().nextInt(online.length)];
			// TODO - record per-type amount spawned and spawn according types.
			if (creatureNPCs.size() <= Constants.maxEvilNPCs - 1) {
				HumanNPC npc = spawnEvil(player.getLocation());
				Messaging.log("" + (npc == null));
				if (npc != null) {
					npc.getInventory().setItemInHand(
							new ItemStack(weapons[new Random()
									.nextInt(weapons.length)], 1));
					npc.setEvil(true);
					npc.getHandle().setRandomPather(true);
					creatureNPCs.put(npc.getPlayer().getEntityId(),
							(CreatureNPC) npc.getHandle());
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
					// TODO: find a way to make this per-type.
					if (world.getBlockTypeIdAt(x, y - 1, z) != 0
							&& world.getBlockTypeIdAt(x, y, z) == 0
							&& world.getBlockTypeIdAt(x, y + 1, z) == 0) {
						if (world.isChunkLoaded(world.getChunkAt(x, z))) {
							if (spaceEntityFree(world.getChunkAt(x, z), x, y, z)) {
								Messaging.log("Success?");
								return NPCSpawner.spawnBasicHumanNpc(0,
										UtilityProperties.getRandomName(),
										loc.getWorld(), x, y, z,
										random.nextInt(360), 0);
							} else
								Messaging.log("Failed at 3");
						} else
							Messaging.log("Failed at 2");
					} else
						Messaging.log("Failed at 1");
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
		for (CreatureNPC creature : creatureNPCs.values()) {
			NPCSpawner.removeBasicHumanNpc(creature.npc);
		}
	}

	public static void despawn(CreatureNPC npc) {
		creatureNPCs.remove(npc.getBukkitEntity().getEntityId());
	}

	public static void onDamage(Entity entity, EntityDamageEvent event) {
		creatureNPCs.get(entity.getEntityId()).onDamage(event);
	}

	public static void onEntityDeath(Entity entity) {
		if (creatureNPCs.get(entity.getEntityId()) != null) {
			CreatureNPC creatureNPC = creatureNPCs.get(entity.getEntityId());
			creatureNPC.onDeath();
			NPCSpawner.removeBasicHumanNpc(creatureNPC.npc);
			creatureNPCs.remove(entity.getEntityId());
		}
	}

	public static CreatureNPC getEvil(Entity entity) {
		return creatureNPCs.get(entity.getEntityId());
	}

	public static class EvilTick implements Runnable {
		@Override
		public void run() {
			for (CreatureNPC npc : creatureNPCs.values()) {
				npc.doTick();
			}
		}
	}
}