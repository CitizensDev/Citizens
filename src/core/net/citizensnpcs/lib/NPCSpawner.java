package net.citizensnpcs.lib;

import java.lang.reflect.Field;
import java.util.Map;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.lib.creatures.CreatureNPCType;
import net.citizensnpcs.utils.Messaging;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class NPCSpawner {
	private static Map<String, CraftPlayer> playerMap = getPlayerMap();

	private static void clearMap(String name) {
		if (playerMap != null)
			playerMap.remove(ChatColor.stripColor(name));
	}

	public static CraftNPC createNPC(String name, Location loc) {
		if (loc == null || loc.getWorld() == null) {
			Messaging.log("Null location or world while spawning", name,
					". Is the location unloaded or missing?");
			return null;
		}
		WorldServer ws = getWorldServer(loc.getWorld());
		clearMap(name);
		CraftNPC eh = new CraftNPC(getMinecraftServer(ws.getServer()), ws,
				name, new ItemInWorldManager(ws));
		eh.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), loc.getPitch());
		return eh;
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

	private static MinecraftServer getMinecraftServer(Server server) {
		return ((CraftServer) server).getServer();
	}

	@SuppressWarnings("unchecked")
	private static Map<String, CraftPlayer> getPlayerMap() {
		try {
			Field f = CraftEntity.class.getDeclaredField("players");
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

	public static void removeNPCFromPlayerList(CraftNPC npc) {
		npc.world.players.remove(npc);
	}

	public static void removeNPCFromPlayerList(HumanNPC npc) {
		getWorldServer(npc.getWorld()).players.remove(npc.getHandle());
	}

	public static CraftNPC spawnNPC(Location loc, CreatureNPCType type) {
		try {
			String name = type.chooseRandomName();
			WorldServer ws = getWorldServer(loc.getWorld());
			clearMap(name);
			CraftNPC eh = createNPC(name, loc);
			ws.addEntity(eh);
			ws.players.remove(eh);
			return eh;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}