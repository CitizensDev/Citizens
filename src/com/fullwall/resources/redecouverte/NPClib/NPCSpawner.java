package com.fullwall.resources.redecouverte.NPClib;

import java.lang.reflect.Field;
import net.minecraft.server.Entity;
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

	protected static WorldServer GetWorldServer(World world) {
		try {
			CraftWorld w = (CraftWorld) world;
			Field f;
			f = CraftWorld.class.getDeclaredField("world");

			f.setAccessible(true);
			return (WorldServer) f.get(w);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static MinecraftServer GetMinecraftServer(Server server) {

		if (server instanceof CraftServer) {
			CraftServer cs = (CraftServer) server;
			Field f;
			try {
				f = CraftServer.class.getDeclaredField("console");
			} catch (NoSuchFieldException ex) {
				return null;
			} catch (SecurityException ex) {
				return null;
			}
			MinecraftServer ms;
			try {
				f.setAccessible(true);
				ms = (MinecraftServer) f.get(cs);
			} catch (IllegalArgumentException ex) {
				return null;
			} catch (IllegalAccessException ex) {
				return null;
			}
			return ms;
		}
		return null;
	}

	public static HumanNPC SpawnBasicHumanNpc(int UID, String name,
			World world, double x, double y, double z, float yaw, float pitch) {
		try {
			WorldServer ws = GetWorldServer(world);
			MinecraftServer ms = GetMinecraftServer(ws.getServer());

			CraftNPC eh = new CraftNPC(ms, ws, name, new ItemInWorldManager(ws));
			eh.setPositionRotation(x, y, z, yaw, pitch);

			ws.addEntity(eh);

			return new HumanNPC(eh, UID, name);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void RemoveBasicHumanNpc(HumanNPC npc) {
		try {
			npc.getMCEntity().world.removeEntity(npc.getMCEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static LivingEntity SpawnMob(CreatureType type, World world,
			double x, double y, double z) {
		try {
			WorldServer ws = GetWorldServer(world);

			Entity eh = EntityTypes.a(type.getName(), ws);
			eh.setPositionRotation(x, y, z, 0, 0);
			// ws.a(eh)?
			ws.addEntity(eh);

			return (LivingEntity) eh.getBukkitEntity();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
