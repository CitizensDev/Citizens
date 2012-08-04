package net.citizensnpcs.listeners;

import java.util.Map;

import net.citizensnpcs.api.event.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.api.event.NPCRemoveEvent.NPCRemoveReason;
import net.citizensnpcs.misc.NPCLocation;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.creatures.CreatureNPC;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.google.common.collect.MapMaker;

public class WorldListen implements Listener {
    private final Map<NPCLocation, Integer> toRespawn = new MapMaker().makeMap();

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        // Respawns any existing NPCs in the loaded chunk
        for (NPCLocation tempLoc : toRespawn.keySet()) {
            if (event.getWorld().equals(tempLoc.getLocation().getWorld())
                    && tempLoc.getChunkX() == event.getChunk().getX() && tempLoc.getChunkZ() == event.getChunk().getZ()) {
                if (NPCManager.get(tempLoc.getUID()) != null) {
                    NPCManager.register(tempLoc.getUID(), tempLoc.getOwner(), NPCCreateReason.RESPAWN);
                }
                toRespawn.remove(tempLoc);
                Messaging.debug("Reloaded", tempLoc.getUID(), "due to chunk load at", tempLoc.getChunkX(),
                        tempLoc.getChunkZ());
            }
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (event.isCancelled())
            return;
        // Stores NPC location/name for later respawn.
        for (HumanNPC npc : NPCManager.getList().values()) {
            if (event.getWorld().equals(npc.getWorld()) && event.getChunk().getX() == npc.getChunkX()
                    && event.getChunk().getZ() == npc.getChunkZ()) {
                NPCLocation loc = new NPCLocation(npc.getLocation(), npc.getUID(), npc.getOwner());
                toRespawn.put(loc, npc.getUID());
                PropertyManager.save(npc);
                NPCManager.safeDespawn(npc);
                Messaging.debug("Despawned", npc.getUID(), "due to chunk unload at", npc.getChunkX(), npc.getChunkZ());
            }
        }
        for (CreatureNPC entry : CreatureTask.creatureNPCs.values()) {
            if (entry.getBukkitEntity().getLocation().getBlock().getChunk().equals(event.getChunk())) {
                CreatureTask.despawn(entry, NPCRemoveReason.UNLOAD);
            }
        }
    }
}