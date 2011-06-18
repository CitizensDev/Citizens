package com.fullwall.Citizens.Listeners;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.CreatureTask;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.Misc.NPCLocation;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.Creatures.CreatureNPC;

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
			if (npc != null
					&& npc.getLocation().getBlock().getChunk()
							.equals(event.getChunk())) {
				NPCLocation loc = new NPCLocation(npc.getLocation(),
						npc.getUID(), npc.getOwner());
				toRespawn.put(loc, npc.getUID());
				NPCManager.despawn(entry);
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
			if (event.getChunk().getWorld()
					.getChunkAt(tempLoc.getX(), tempLoc.getZ())
					.equals(event.getChunk())) {
				NPCManager.register(tempLoc.getUID(), tempLoc.getOwner());
				toRespawn.remove(tempLoc);
			}
		}
	}
}