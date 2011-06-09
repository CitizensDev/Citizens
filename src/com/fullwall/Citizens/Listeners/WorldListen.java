package com.fullwall.Citizens.Listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.Misc.NPCLocation;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WorldListen extends WorldListener implements Listener {
	private final Citizens plugin;
	private final List<NPCLocation> toRespawn = Collections
			.synchronizedList(new ArrayList<NPCLocation>());
	private PluginManager pm;

	public WorldListen(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
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
		for (Integer entry : NPCManager.GlobalUIDs.keySet()) {
			HumanNPC npc = NPCManager.get(entry);
			if (npc != null
					&& npc.getLocation().getBlock().getChunk()
							.equals(e.getChunk())) {
				NPCLocation loc = new NPCLocation(npc.getLocation(),
						npc.getUID(), npc.getOwner());
				synchronized (toRespawn) {
					toRespawn.add(loc);
				}
				NPCManager.despawn(entry);
			}
		}
	}

	@Override
	public void onChunkLoad(ChunkLoadEvent e) {
		// Respawns any existing NPCs in the loaded chunk
		synchronized (toRespawn) {
			for (NPCLocation tempLoc : toRespawn) {
				if (e.getChunk().getWorld()
						.getChunkAt(tempLoc.getX(), tempLoc.getZ())
						.equals(e.getChunk())) {
					NPCManager.register(tempLoc.getUID(), tempLoc.getOwner());
					toRespawn.remove(tempLoc);
				}
			}
		}
	}
}