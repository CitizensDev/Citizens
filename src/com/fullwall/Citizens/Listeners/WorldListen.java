package com.fullwall.Citizens.Listeners;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.event.Event;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.NPCLocation;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WorldListen extends WorldListener implements Listener {
	private Citizens plugin;
	private ConcurrentHashMap<NPCLocation, String> toRespawn = new ConcurrentHashMap<NPCLocation, String>();
	private PluginManager pm;

	public WorldListen(Citizens plugin) {
		this.plugin = plugin;
	}

	/**
	 * Register world events
	 */
	public void registerEvents() {
		pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Event.Type.CHUNK_UNLOAD, this, Event.Priority.Normal,
				plugin);
		pm.registerEvent(Event.Type.CHUNK_LOAD, this, Event.Priority.Normal,
				plugin);
	}

	@Override
	public void onChunkUnload(ChunkUnloadEvent e) {
		// Stores NPC location/name for later respawn.
		for (Entry<Integer, String> i : NPCManager.GlobalUIDs.entrySet()) {
			HumanNPC npc = NPCManager.get(i.getKey());
			if (npc != null
					&& npc.getLocation().getBlock().getChunk()
							.equals(e.getChunk())) {
				NPCLocation loc = new NPCLocation(plugin, npc.getLocation(),
						npc.getUID(), npc.getOwner());
				toRespawn.put(loc, i.getValue());
				plugin.basicNPCHandler.despawn(i.getKey());
			}
		}
	}

	@Override
	public void onChunkLoad(ChunkLoadEvent e) {
		// Respawns any existing NPCs in the loaded chunk
		for (Entry<NPCLocation, String> i : toRespawn.entrySet()) {
			NPCLocation tempLoc = i.getKey();
			if (e.getChunk().getWorld()
					.getChunkAt(tempLoc.getX(), tempLoc.getZ())
					.equals(e.getChunk())) {
				NPCManager.register(i.getValue(), tempLoc.getUID(),
						tempLoc.getOwner());
				toRespawn.remove(tempLoc);
			}
		}
	}
}
