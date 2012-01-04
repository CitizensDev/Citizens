package net.citizensnpcs.lib.creatures;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import net.citizensnpcs.lib.NPCSpawner;
import net.citizensnpcs.utils.ByIdArray;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class CreatureTask implements Runnable {
	private void onSpawn(CreatureNPC creatureNPC) {
		creatureNPC.onSpawn();
	}

	public final static ByIdArray<CreatureNPC> creatureNPCs = ByIdArray
			.create();
	private final static Map<CreatureNPCType, Integer> spawned = new EnumMap<CreatureNPCType, Integer>(
			CreatureNPCType.class);
	private Player[] online;

	@Override
	public void run() {
		if (dirty) {
			online = Bukkit.getServer().getOnlinePlayers();
			dirty = false;
		}
		if (online != null && online.length > 0) {
			Player player = online[random.nextInt(online.length)];
			CreatureNPCType type = CreatureNPCType.getRandomType(random);
			if (type == null)
				return;
			// TODO: favour certain biomes perhaps?
			spawnCreature(type, player.getLocation());
		}
	}

	private void spawnCreature(CreatureNPCType type, Location location) {
		if (spawned.get(type) == null) {
			spawned.put(type, 0);
		} else if (type.canSpawn(spawned.get(type))) {
			CreatureNPC npc = type.spawn(location);
			if (npc != null) {
				spawned.put(type, spawned.get(type) + 1);
				creatureNPCs.put(npc.getPlayer().getEntityId(), npc);
				onSpawn(creatureNPCs.get(npc.getPlayer().getEntityId()));
			}
		}
	}

	public static class CreatureTick implements Runnable {
		@Override
		public void run() {
			for (CreatureNPC npc : creatureNPCs) {
				NPCSpawner.removeNPCFromPlayerList(npc.getHandle());
				npc.doTick();
			}
		}
	}

	private static boolean dirty = true;

	private static Random random = new Random(System.currentTimeMillis());

	public static void despawn(CreatureNPC npc) {
		removeFromMaps(npc);
		npc.getHandle().die();
		npc.getHandle().world.removeEntity(npc.getHandle());
	}

	public static void despawnAll() {
		for (CreatureNPC creature : creatureNPCs) {
			despawn(creature);
		}
	}

	public static CreatureNPC getCreature(Entity entity) {
		return creatureNPCs.get(entity.getEntityId());
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
			despawn(creatureNPC);
		}
	}

	public static void removeFromMaps(CreatureNPC npc) {
		creatureNPCs.remove(npc.getPlayer().getEntityId());
		spawned.put(npc.getType(), spawned.get(npc.getType()) - 1);
	}

	public static void setDirty() {
		dirty = true;
	}
}