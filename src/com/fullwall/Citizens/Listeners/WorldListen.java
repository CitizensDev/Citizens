package com.fullwall.Citizens.Listeners;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCManager;
import com.fullwall.Citizens.Utils.NPCLocation;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WorldListen extends WorldListener {

	private Citizens plugin;
	private HashMap<NPCLocation, String> toRespawn = new HashMap<NPCLocation, String>();

	public WorldListen(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onChunkUnloaded(ChunkUnloadEvent e) {
		for (Entry<Integer, String> i : NPCManager.GlobalUIDs.entrySet()) {
			HumanNPC npc = NPCManager.getNPC(i.getKey());
			if (npc != null
					&& npc.getBukkitEntity().getLocation().getBlock()
							.getChunk().equals(e.getChunk())) {
				NPCLocation loc = new NPCLocation(plugin, npc.getBukkitEntity()
						.getLocation());
				toRespawn.put(loc, i.getValue());
				plugin.handler.despawnNPC(i.getKey());
			}
		}
	}

	public void onChunkLoaded(ChunkLoadEvent e) {
		for (Entry<NPCLocation, String> i : toRespawn.entrySet()) {
			if (e.getChunk().getWorld()
					.getChunkAt(i.getKey().getX(), i.getKey().getZ())
					.equals(e.getChunk())) {
				plugin.handler.spawnNPC(i.getValue(), i.getKey().getLocation());
			}
		}
	}

}
