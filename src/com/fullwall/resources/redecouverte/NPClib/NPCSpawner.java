package com.fullwall.resources.redecouverte.NPClib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MathHelper;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HumanNPC SpawnBasicHumanNpc(String uniqueId, String name,
			World world, double x, double y, double z, float yaw, float pitch, int UID) {
		try {
			WorldServer ws = GetWorldServer(world);
			MinecraftServer ms = GetMinecraftServer(ws.getServer());

			CraftNPC eh = new CraftNPC(ms, ws, name, new ItemInWorldManager(ws));
			eh.c(x, y, z, yaw, pitch);

			int m = MathHelper.b(eh.locX / 16.0D);
			int n = MathHelper.b(eh.locZ / 16.0D);

			ws.c(m, n).a(eh);
			ws.b.add(eh);

			// ws.b(eh);
			Class params[] = new Class[1];
			params[0] = Entity.class;

			Method method;
			method = net.minecraft.server.World.class.getDeclaredMethod("b",
					params);
			method.setAccessible(true);
			Object margs[] = new Object[1];
			margs[0] = eh;
			method.invoke(ws, margs);

			return new HumanNPC(eh, uniqueId, name, UID);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void RemoveBasicHumanNpc(HumanNPC npc) {
		try {
			npc.getMCEntity().world.e(npc.getMCEntity());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static LivingEntity SpawnMob(CreatureType type, World world,
			double x, double y, double z) {
		try {
			WorldServer ws = GetWorldServer(world);

			Entity eh = EntityTypes.a(type.getName(), ws);
			eh.c(x, y, z, 0, 0);
			ws.a(eh);

			return (LivingEntity) eh.getBukkitEntity();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
