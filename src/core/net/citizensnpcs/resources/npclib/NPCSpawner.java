package net.citizensnpcs.resources.npclib;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.event.NPCRemoveEvent;
import net.citizensnpcs.api.event.NPCRemoveEvent.NPCRemoveReason;
import net.citizensnpcs.resources.npclib.creatures.CreatureNPCType;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.PacketUtils;
import net.minecraft.server.v1_6_R3.MinecraftServer;
import net.minecraft.server.v1_6_R3.Packet29DestroyEntity;
import net.minecraft.server.v1_6_R3.PlayerInteractManager;
import net.minecraft.server.v1_6_R3.World;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_6_R3.CraftServer;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;

public class NPCSpawner {
    public static void despawnNPC(CraftNPC npc, NPCRemoveReason reason) {
        despawnNPC(npc.npc, reason);
    }

    public static void despawnNPC(HumanNPC npc, NPCRemoveReason reason) {
        if (getWorldServer(npc.getWorld()).getEntity(npc.getPlayer().getEntityId()) != npc.getHandle()
                || npc.getHandle().dead)
            return;
        Bukkit.getServer().getPluginManager().callEvent(new NPCRemoveEvent(npc, reason));
        PacketUtils.sendPacketToOnline(new Packet29DestroyEntity(npc.getHandle().id), null);
        npc.getHandle().die();
    }

    private static MinecraftServer getMinecraftServer(Server server) {
        return ((CraftServer) server).getServer();
    }

    private static World getWorldServer(org.bukkit.World world) {
        return ((CraftWorld) world).getHandle();
    }

    public static HumanNPC spawnNPC(final HumanNPC npc, final Location loc) {
        World ws = getWorldServer(loc.getWorld());
        npc.getHandle().setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ws.addEntity(npc.getHandle());
        Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin, new Runnable() {
            @Override
            public void run() {
                npc.getHandle().aA = loc.getYaw();
            }
        }, 2);
        ws.players.remove(npc.getHandle());
        return npc;
    }

    public static HumanNPC spawnNPC(int UID, String name, final Location loc) {
        if (loc == null || loc.getWorld() == null) {
            Messaging.log("Null location or world while spawning", name, "UID", UID
                    + ". Is the location unloaded or missing?");
            return null;
        }
        World ws = getWorldServer(loc.getWorld());
        final CraftNPC eh = new CraftNPC(getMinecraftServer(ws.getServer()), ws, name, new PlayerInteractManager(ws));
        eh.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin, new Runnable() {
            @Override
            public void run() {
                eh.aA = loc.getYaw();
            }
        });
        ws.addEntity(eh);
        ws.players.remove(eh);
        return new HumanNPC(eh, UID, name);
    }

    public static HumanNPC spawnNPC(final Location loc, CreatureNPCType type) {
        try {
            String name = type.chooseRandomName();
            World ws = getWorldServer(loc.getWorld());
            final CraftNPC eh = type.getEntityConstructor().newInstance(getMinecraftServer(ws.getServer()), ws, name,
                    new PlayerInteractManager(ws));
            eh.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin, new Runnable() {
                @Override
                public void run() {
                    eh.aA = loc.getYaw();
                }
            });
            ws.addEntity(eh);
            ws.players.remove(eh);
            return new HumanNPC(eh, -1 /*Fake UID*/, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}