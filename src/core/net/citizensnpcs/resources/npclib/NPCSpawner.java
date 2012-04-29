package net.citizensnpcs.resources.npclib;

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

public class NPCSpawner {
    private static WorldServer getWorldServer(World world) {
        return ((CraftWorld) world).getHandle();
    }

    private static MinecraftServer getMinecraftServer(Server server) {
        return ((CraftServer) server).getServer();
    }

    public static HumanNPC spawnNPC(int UID, String name, Location loc) {
        if (loc == null || loc.getWorld() == null) {
            Messaging.log("Null location or world while spawning", name, "UID", UID
                    + ". Is the location unloaded or missing?");
            return null;
        }
        WorldServer ws = getWorldServer(loc.getWorld());
        CraftNPC eh = new CraftNPC(getMinecraftServer(ws.getServer()), ws, name, new ItemInWorldManager(ws));
        eh.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        eh.X = loc.getYaw();
        ws.addEntity(eh);
        ws.players.remove(eh);
        return new HumanNPC(eh, UID, name);
    }

    public static HumanNPC spawnNPC(Location loc, CreatureNPCType type) {
        try {
            String name = type.chooseRandomName();
            WorldServer ws = getWorldServer(loc.getWorld());
            CraftNPC eh = type.getEntityConstructor().newInstance(getMinecraftServer(ws.getServer()), ws, name,
                    new ItemInWorldManager(ws));
            eh.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
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
        npc.getHandle().setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ws.addEntity(npc.getHandle());
        ws.players.remove(npc.getHandle());
        return npc;
    }

    public static void despawnNPC(HumanNPC npc, NPCRemoveReason reason) {
        if (getWorldServer(npc.getWorld()).getEntity(npc.getPlayer().getEntityId()) != npc.getHandle()
                || npc.getHandle().dead)
            return;
        Bukkit.getServer().getPluginManager().callEvent(new NPCRemoveEvent(npc, reason));
        PacketUtils.sendPacketToOnline(new Packet29DestroyEntity(npc.getHandle().id), null);
        npc.getHandle().die();
    }

    public static void despawnNPC(CraftNPC npc, NPCRemoveReason reason) {
        despawnNPC(npc.npc, reason);
    }

    public static void removeNPCFromPlayerList(HumanNPC npc) {
        getWorldServer(npc.getWorld()).players.remove(npc.getHandle());
    }
}