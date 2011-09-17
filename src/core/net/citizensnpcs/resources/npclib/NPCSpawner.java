package net.citizensnpcs.resources.npclib;

import net.citizensnpcs.api.event.npc.NPCRemoveEvent;
import net.citizensnpcs.api.event.npc.NPCRemoveEvent.NPCRemoveReason;
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
	private static WorldServer getWorldServer(World world) {
		return ((CraftWorld) world).getHandle();
	}

	private static MinecraftServer getMinecraftServer(Server server) {
		return ((CraftServer) server).getServer();
	}

	public static HumanNPC spawnNPC(int UID, String name, Location loc) {
		WorldServer ws = getWorldServer(loc.getWorld());
		CraftNPC eh = new CraftNPC(getMinecraftServer(ws.getServer()), ws,
				name, new ItemInWorldManager(ws));
		ws.addEntity(eh);
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
			return new HumanNPC(eh, -1 /*Fake UID*/, name);
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
		Bukkit.getServer().getPluginManager()
				.callEvent(new NPCRemoveEvent(npc, reason));
		npc.getPlayer().remove();
	}

	public static void despawnNPC(CraftNPC npc, NPCRemoveReason reason) {
		despawnNPC(npc.npc, reason);
	}

	public static void removeNPCFromPlayerList(HumanNPC npc) {
		getWorldServer(npc.getWorld()).players.remove(npc.getHandle());
	}
}