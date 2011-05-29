package com.fullwall.resources.redecouverte.NPClib;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.CreatureType;

public class NPCSpawner {

	protected static WorldServer getWorldServer(World world) {
		if (world instanceof CraftWorld) {
			CraftWorld w = (CraftWorld) world;
			return w.getHandle();
		}
		return null;
	}

	private static MinecraftServer getMinecraftServer(Server server) {
		if (server instanceof CraftServer) {
			CraftServer cs = (CraftServer) server;
			return cs.getServer();
		}
		return null;
	}

	public static HumanNPC spawnBasicHumanNpc(int UID, String name,
			World world, double x, double y, double z, float yaw, float pitch) {
		try {
			WorldServer ws = getWorldServer(world);
			MinecraftServer ms = getMinecraftServer(ws.getServer());
			CraftNPC eh = new CraftNPC(ms, ws, name, new ItemInWorldManager(ws));
			eh.setPositionRotation(x, y, z, yaw, pitch);
			ws.addEntity(eh);
			ws.players.remove((EntityHuman) eh);
			return new HumanNPC(eh, UID, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void removeBasicHumanNpc(HumanNPC npc) {
		try {
			npc.getMCEntity().world.removeEntity(npc.getMCEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static LivingEntity spawnMob(CreatureType type, World world,
			double x, double y, double z) {
		try {
			WorldServer ws = getWorldServer(world);
			Entity eh = EntityTypes.a(type.getName(), ws);
			eh.setPositionRotation(x, y, z, 0, 0);
			ws.addEntity(eh);
			return (LivingEntity) eh.getBukkitEntity();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void removeNPCFromPlayerList(CraftNPC npc) {
		getWorldServer(npc.getBukkitEntity().getWorld()).players
				.remove((EntityHuman) npc);
	}
}