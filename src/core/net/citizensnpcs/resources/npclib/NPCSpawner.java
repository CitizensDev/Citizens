package net.citizensnpcs.resources.npclib;

import net.citizensnpcs.api.events.NPCRemoveEvent;
import net.citizensnpcs.api.events.NPCRemoveEvent.NPCRemoveReason;
import net.citizensnpcs.resources.npclib.creatures.CreatureNPCType;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;

public class NPCSpawner {
	protected static WorldServer getWorldServer(World world) {
		if (world instanceof CraftWorld) {
			return ((CraftWorld) world).getHandle();
		}
		return null;
	}

	private static MinecraftServer getMinecraftServer(Server server) {
		if (server instanceof CraftServer) {
			return ((CraftServer) server).getServer();
		}
		return null;
	}

	public static HumanNPC spawnNPC(int UID, String name, World world,
			double x, double y, double z, float yaw, float pitch) {
		try {
			WorldServer ws = getWorldServer(world);
			MinecraftServer ms = getMinecraftServer(ws.getServer());
			CraftNPC eh = new CraftNPC(ms, ws, name, new ItemInWorldManager(ws));
			eh.setLocation(x, y, z, yaw, pitch);
			ws.addEntity(eh);
			ws.players.remove(eh);
			return new HumanNPC(eh, UID, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HumanNPC spawnNPC(int UID, String name, World world,
			double x, double y, double z, float yaw, float pitch,
			CreatureNPCType type) {
		try {
			WorldServer ws = getWorldServer(world);
			MinecraftServer ms = getMinecraftServer(ws.getServer());
			CraftNPC eh = (CraftNPC) type.getEntityClass().getConstructors()[0]
					.newInstance(ms, ws, name, new ItemInWorldManager(ws));
			ws.addEntity(eh);
			eh.setLocation(x, y, z, yaw, pitch);
			ws.players.remove(eh);
			return new HumanNPC(eh, UID, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HumanNPC spawnNPC(HumanNPC npc, Location loc) {
		try {
			WorldServer ws = getWorldServer(loc.getWorld());
			ws.addEntity(npc.getHandle());
			npc.teleport(loc);
			ws.players.remove(npc.getHandle());
			return npc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void despawnNPC(HumanNPC npc, NPCRemoveReason reason) {
		despawnNPC(npc.getHandle(), reason);
	}

	public static void despawnNPC(CraftNPC npc, NPCRemoveReason reason) {
		try {
			Bukkit.getServer().getPluginManager()
					.callEvent(new NPCRemoveEvent(npc.npc, reason));
			npc.world.removeEntity(npc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeNPCFromPlayerList(HumanNPC npc) {
		getWorldServer(npc.getPlayer().getWorld()).players.remove(npc
				.getHandle());
	}
}