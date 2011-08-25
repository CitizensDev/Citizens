package net.citizensnpcs.resources.npclib;

import net.citizensnpcs.api.events.NPCRemoveEvent;
import net.citizensnpcs.api.events.NPCRemoveEvent.NPCRemoveReason;
import net.citizensnpcs.properties.properties.UtilityProperties;
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

	public static HumanNPC spawnNPC(int UID, String name, Location loc) {
		WorldServer ws = getWorldServer(loc.getWorld());
		CraftNPC eh = new CraftNPC(getMinecraftServer(ws.getServer()), ws,
				name, new ItemInWorldManager(ws));
		ws.addEntity(eh);
		ws.players.remove(eh);
		eh.getBukkitEntity().teleport(loc);
		return new HumanNPC(eh, UID, name);
	}

	public static HumanNPC spawnNPC(Location loc, CreatureNPCType type) {
		try {
			String name = UtilityProperties.getRandomName(type);
			WorldServer ws = getWorldServer(loc.getWorld());
			CraftNPC eh = type.getEntityConstructor().newInstance(
					getMinecraftServer(ws.getServer()), ws, name,
					new ItemInWorldManager(ws));
			ws.addEntity(eh);
			eh.getBukkitEntity().teleport(loc);
			ws.players.remove(eh);
			return new HumanNPC(eh, -1, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HumanNPC spawnNPC(HumanNPC npc, Location loc) {
		WorldServer ws = getWorldServer(loc.getWorld());
		ws.addEntity(npc.getHandle());
		npc.teleport(loc);
		return npc;
	}

	public static void despawnNPC(HumanNPC npc, NPCRemoveReason reason) {
		despawnNPC(npc.getHandle(), reason);
	}

	public static void despawnNPC(CraftNPC npc, NPCRemoveReason reason) {
		Bukkit.getServer().getPluginManager().callEvent(new NPCRemoveEvent(npc.npc, reason));
		npc.world.removeEntity(npc);
	}

	public static void removeNPCFromPlayerList(HumanNPC npc) {
		getWorldServer(npc.getPlayer().getWorld()).players.remove(npc.getHandle());
	}
}