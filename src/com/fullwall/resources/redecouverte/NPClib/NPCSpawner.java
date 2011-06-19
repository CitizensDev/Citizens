package com.fullwall.resources.redecouverte.NPClib;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;

import com.fullwall.resources.redecouverte.NPClib.Creatures.CreatureNPCType;

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

	public static HumanNPC spawnBasicHumanNpc(int UID, String name,
			World world, double x, double y, double z, float yaw, float pitch) {
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

	public static HumanNPC spawnBasicHumanNpc(int UID, String name,
			World world, double x, double y, double z, float yaw, float pitch,
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

	public static void removeBasicHumanNpc(HumanNPC npc) {
		try {
			npc.getHandle().world.removeEntity(npc.getHandle());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeNPCFromPlayerList(HumanNPC npc) {
		getWorldServer(npc.getPlayer().getWorld()).players.remove(npc
				.getHandle());
	}
}