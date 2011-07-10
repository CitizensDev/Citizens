package com.citizens.listeners;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.PluginManager;

import com.citizens.Citizens;
import com.citizens.CreatureTask;
import com.citizens.interfaces.Listener;
import com.citizens.misc.NPCLocation;
import com.citizens.npcs.NPCManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.resources.npclib.creatures.CreatureNPC;
import com.citizens.utils.Messaging;

public class WorldListen extends WorldListener implements Listener {
	private final ConcurrentHashMap<NPCLocation, Integer> toRespawn = new ConcurrentHashMap<NPCLocation, Integer>();

	@Override
	public void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Event.Type.CHUNK_UNLOAD, this, Event.Priority.Normal,
				Citizens.plugin);
		pm.registerEvent(Event.Type.CHUNK_LOAD, this, Event.Priority.Normal,
				Citizens.plugin);
	}

	@Override
	public void onChunkUnload(ChunkUnloadEvent event) {
		// Stores NPC location/name for later respawn.
		for (Integer entry : NPCManager.GlobalUIDs.keySet()) {
			HumanNPC npc = NPCManager.get(entry);
			if (event.getWorld().getChunkAt(npc.getLocation())
					.equals(event.getChunk())) {
				Messaging.debug("Despawned at", event.getChunk().getX(), event
						.getChunk().getZ());
				NPCLocation loc = new NPCLocation(npc.getLocation(),
						npc.getUID(), npc.getOwner());
				toRespawn.put(loc, npc.getUID());
				NPCManager.removeForRespawn(entry);
			}
		}
		for (CreatureNPC entry : CreatureTask.creatureNPCs.values()) {
			if (entry.getBukkitEntity().getLocation().getBlock().getChunk()
					.equals(event.getChunk())) {
				CreatureTask.despawn(entry);
			}
		}
	}

	@Override
	public void onChunkLoad(ChunkLoadEvent event) {
		// Respawns any existing NPCs in the loaded chunk
		for (NPCLocation tempLoc : toRespawn.keySet()) {
			if (tempLoc.getChunkX() == event.getChunk().getX()
					&& tempLoc.getChunkZ() == event.getChunk().getZ()) {
				Messaging.debug("Reloaded at", tempLoc.getChunkX(),
						tempLoc.getChunkZ());
				if (NPCManager.get(tempLoc.getUID()) != null)
					NPCManager.register(tempLoc.getUID(), tempLoc.getOwner());
				toRespawn.remove(tempLoc);
			}
		}
	}
}