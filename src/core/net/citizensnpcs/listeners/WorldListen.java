package net.citizensnpcs.listeners;

import java.util.Map;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.events.NPCRemoveEvent.NPCRemoveReason;
import net.citizensnpcs.misc.NPCLocation;
import net.citizensnpcs.npcs.NPCManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.creatures.CreatureNPC;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.PluginManager;

import com.google.common.collect.MapMaker;

public class WorldListen extends WorldListener implements Listener {
	private final Map<NPCLocation, Integer> toRespawn = new MapMaker()
			.makeMap();

	@Override
	public void registerEvents(Citizens plugin) {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Type.CHUNK_UNLOAD, this, Priority.Normal, plugin);
		pm.registerEvent(Type.CHUNK_LOAD, this, Priority.Normal, plugin);
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
				CreatureTask.despawn(entry, NPCRemoveReason.UNLOAD);
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