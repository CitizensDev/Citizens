package com.citizens.listeners;

import java.util.Map;

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
import com.google.common.collect.MapMaker;

public class WorldListen extends WorldListener implements Listener {
	private final Map<NPCLocation, Integer> toRespawn = new MapMaker()
			.makeMap();

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
			if (event.getChunk().getX() == npc.getChunkX()
					&& event.getChunk().getZ() == npc.getChunkZ()) {
				Messaging.debug("Despawned", npc.getUID(), "at", event
						.getChunk().getX(), event.getChunk().getZ(),
						"(" + npc.getChunkX(), npc.getChunkZ() + ")");
				NPCLocation loc = new NPCLocation(npc.getLocation(),
						npc.getUID(), npc.getOwner());
				toRespawn.put(loc, npc.getUID());
				NPCManager.safeDespawn(npc);
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
				if (NPCManager.get(tempLoc.getUID()) != null) {
					Messaging.debug("Reloaded", tempLoc.getUID(), "at",
							tempLoc.getChunkX(), tempLoc.getChunkZ());
					NPCManager.register(tempLoc.getUID(), tempLoc.getOwner());
				}
				toRespawn.remove(tempLoc);
			}
		}
	}
}