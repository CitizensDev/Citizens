package com.fullwall.Citizens.Listeners;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.NPCLocation;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WorldListen extends WorldListener {

	private Citizens plugin;
	private ConcurrentHashMap<NPCLocation, String> toRespawn = new ConcurrentHashMap<NPCLocation, String>();

	public WorldListen(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onChunkLoad(ChunkLoadEvent e) {
		for (Entry<NPCLocation, String> i : toRespawn.entrySet()) {
			if (e.getChunk().getWorld()
					.getChunkAt(i.getKey().getX(), i.getKey().getZ())
					.equals(e.getChunk())) {
				plugin.handler.spawnExistingNPC(i.getValue(), i.getKey()
						.getUID());
				toRespawn.remove(i.getKey());
			}
		}
	}

	@Override
	public void onChunkUnload(ChunkUnloadEvent e) {
		for (Entry<Integer, String> i : NPCManager.GlobalUIDs.entrySet()) {
			HumanNPC npc = NPCManager.getNPC(i.getKey());
			if (npc != null
					&& npc.getBukkitEntity().getLocation().getBlock()
							.getChunk().equals(e.getChunk())) {
				NPCLocation loc = new NPCLocation(plugin, npc.getBukkitEntity()
						.getLocation(), npc.getUID());
				toRespawn.put(loc, i.getValue());
				plugin.handler.despawnNPC(i.getKey());
			}
		}
	}
}
