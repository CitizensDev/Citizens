package net.citizensnpcs.listeners;

import java.util.Map;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.event.npc.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.api.event.npc.NPCRemoveEvent.NPCRemoveReason;
import net.citizensnpcs.misc.NPCLocation;
import net.citizensnpcs.npcs.NPCManager;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.creatures.CreatureNPC;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;

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
		pm.registerEvent(Type.CHUNK_UNLOAD, this, Priority.Monitor, plugin);
		pm.registerEvent(Type.CHUNK_LOAD, this, Priority.Monitor, plugin);
	}

	@Override
	public void onChunkUnload(ChunkUnloadEvent event) {
		if (event.isCancelled())
			return;
		// Stores NPC location/name for later respawn.
		for (int entry : NPCManager.GlobalUIDs.keySet()) {
			HumanNPC npc = NPCManager.get(entry);
			if (event.getChunk().getX() == npc.getChunkX()
					&& event.getChunk().getZ() == npc.getChunkZ()) {
				NPCLocation loc = new NPCLocation(npc.getLocation(),
						npc.getUID(), npc.getOwner());
				toRespawn.put(loc, npc.getUID());
				PropertyManager.save(npc);
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
					NPCManager.register(tempLoc.getUID(), tempLoc.getOwner(),
							NPCCreateReason.RESPAWN);
				}
				toRespawn.remove(tempLoc);
			}
		}
	}
}