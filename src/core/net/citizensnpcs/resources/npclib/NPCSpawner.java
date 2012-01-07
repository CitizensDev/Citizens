package net.citizensnpcs.resources.npclib;

import java.lang.reflect.Field;
import java.util.Map;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.event.NPCRemoveEvent;
import net.citizensnpcs.api.event.NPCRemoveEvent.NPCRemoveReason;
import net.citizensnpcs.resources.npclib.creatures.CreatureNPCType;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.PacketUtils;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet29DestroyEntity;
import net.minecraft.server.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class NPCSpawner {
	private static Map<String, CraftPlayer> playerMap = getPlayerMap();

	@SuppressWarnings("unchecked")
	private static Map<String, CraftPlayer> getPlayerMap() {
		Field f;
		try {
			f = CraftEntity.class.getDeclaredField("players");
			f.setAccessible(true);
			return (Map<String, CraftPlayer>) f.get(null);
		} catch (Exception ex) {
			Messaging.log("Unable to fetch player map from CraftEntity: "
					+ ex.getMessage()
					+ ". Conflicting names will do funny things.");
		}
		return null;
	}

	private static WorldServer getWorldServer(World world) {
		return ((CraftWorld) world).getHandle();
	}

	private static MinecraftServer getMinecraftServer(Server server) {
		return ((CraftServer) server).getServer();
	}

	private static void clearMap(String name) {
		if (playerMap != null)
			playerMap.remove(name);
	}

	public static void delayedRemove(final String name) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin,
				new Runnable() {
					@Override
					public void run() {
						clearMap(name);
					}
				}, 1);
	}

	public static HumanNPC spawnNPC(int UID, String name, Location loc) {
		if (loc == null || loc.getWorld() == null) {
			Messaging.log("Null location or world while spawning", name, "UID",
					UID + ". Is the location unloaded or missing?");
			return null;
		}
		WorldServer ws = getWorldServer(loc.getWorld());
		clearMap(name);
		CraftNPC eh = new CraftNPC(getMinecraftServer(ws.getServer()), ws,
				name, new ItemInWorldManager(ws));
		eh.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), loc.getPitch());
		ws.addEntity(eh);
		ws.players.remove(eh);
		return new HumanNPC(eh, UID, name);
	}

	public static HumanNPC spawnNPC(Location loc, CreatureNPCType type) {
		try {
			String name = type.chooseRandomName();
			WorldServer ws = getWorldServer(loc.getWorld());
			clearMap(name);
			CraftNPC eh = type.getEntityConstructor().newInstance(
					getMinecraftServer(ws.getServer()), ws, name,
					new ItemInWorldManager(ws));
			eh.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(),
					loc.getYaw(), loc.getPitch());
			ws.addEntity(eh);
			ws.players.remove(eh);
			return new HumanNPC(eh, -1 /*Fake UID*/, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HumanNPC spawnNPC(HumanNPC npc, Location loc) {
		WorldServer ws = getWorldServer(loc.getWorld());
		clearMap(npc.getName());
		npc.getHandle().setPositionRotation(loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), loc.getPitch());
		ws.addEntity(npc.getHandle());
		ws.players.remove(npc.getHandle());
		return npc;
	}

	public static void despawnNPC(HumanNPC npc, NPCRemoveReason reason) {
		if (getWorldServer(npc.getWorld()).getEntity(npc.getHandle().id) != npc
				.getHandle())
			return;
		Bukkit.getServer().getPluginManager()
				.callEvent(new NPCRemoveEvent(npc, reason));
		PacketUtils.sendPacketToOnline(
				new Packet29DestroyEntity(npc.getHandle().id), null);
		npc.getHandle().die();
		getWorldServer(npc.getWorld()).removeEntity(npc.getHandle());
	}

	public static void despawnNPC(CraftNPC npc, NPCRemoveReason reason) {
		despawnNPC(npc.npc, reason);
	}

	public static void removeNPCFromPlayerList(HumanNPC npc) {
		getWorldServer(npc.getWorld()).players.remove(npc.getHandle());
	}
}