package net.citizensnpcs.listeners;

import java.util.Set;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.event.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.lib.creatures.CreatureNPC;
import net.citizensnpcs.lib.creatures.CreatureTask;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.PluginManager;

import com.google.common.collect.Sets;

public class WorldListen extends WorldListener implements Listener {
	private final Set<HumanNPC> toRespawn = Sets.newHashSet();

	@Override
	public void registerEvents(Citizens plugin) {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Type.CHUNK_UNLOAD, this, Priority.Monitor, plugin);
		pm.registerEvent(Type.CHUNK_LOAD, this, Priority.Monitor, plugin);
	}

	@Override
	public void onChunkUnload(ChunkUnloadEvent event) {
		if (event.isCancelled())
			return;
		// Stores NPC location/name for later respawn.
		for (Entity entity : event.getChunk().getEntities()) {
			HumanNPC npc = NPCManager.get(entity);
			if (npc == null)
				continue;
			if (event.getWorld().equals(npc.getWorld())
					&& event.getChunk().getX() == npc.getChunkX()
					&& event.getChunk().getZ() == npc.getChunkZ()) {
				toRespawn.add(npc);
				npc.save();
				NPCManager.despawn(npc.getUID());
				Messaging.debug("Despawned", npc.getUID(),
						"due to chunk unload at", npc.getChunkX(),
						npc.getChunkZ());
			}
		}
		for (CreatureNPC entry : CreatureTask.creatureNPCs.values()) {
			if (!entry.getBukkitEntity().getLocation().getBlock().getChunk()
					.equals(event.getChunk()))
				continue;
			CreatureTask.despawn(entry);
		}
	}

	@Override
	public void onChunkLoad(ChunkLoadEvent event) {
		// Respawns any existing NPCs in the loaded chunk
		for (HumanNPC npc : toRespawn) {
			if (!event.getWorld().equals(npc.getLocation().getWorld())
					|| npc.getChunkX() != event.getChunk().getX()
					|| npc.getChunkZ() != event.getChunk().getZ())
				continue;
			if (NPCManager.get(npc.getUID()) != null) {
				NPCManager.register(npc, NPCCreateReason.RESPAWN);
			}
			toRespawn.remove(npc);
			Messaging.debug("Reloaded", npc.getUID(),
					"due to chunk load at", npc.getChunkX(),
					npc.getChunkZ());
		}
	}
}