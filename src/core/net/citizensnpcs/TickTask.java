package net.citizensnpcs;

import java.util.Map.Entry;

import net.citizensnpcs.api.event.npc.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.misc.ActionManager;
import net.citizensnpcs.misc.ActionManager.CachedAction;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.NPCSpawner;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.PathUtils;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.waypoints.WaypointPath;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TickTask implements Runnable {

	@Override
	public void run() {
		HumanNPC npc;
		int UID;
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			{
				npc = entry.getValue();
				updateWaypoints(npc);
				npc.doTick();
				NPCSpawner.removeNPCFromPlayerList(npc);
				UID = entry.getKey();
				for (Player players : online) {
					String name = players.getName();
					if (!npc.getNPCData().isLookClose()
							&& !npc.getNPCData().isTalkClose())
						continue;
					// If the player is within 'seeing' range
					if (LocationUtils.withinRange(npc.getLocation(),
							players.getLocation(),
							SettingsManager.getDouble("NPCRange"))) {
						if (npc.getHandle().pathFinished()
								&& !npc.getHandle().hasTarget()
								&& npc.getNPCData().isLookClose()) {
							NPCManager.faceEntity(npc, players);
						}
						cacheActions(players, npc, UID, name);
					} else {
						resetActions(UID, name, npc);
					}
				}
			}
		}
	}

	private void updateWaypoints(HumanNPC npc) {
		WaypointPath waypoints = npc.getWaypoints();
		switch (waypoints.size()) {
		case 0:
			break;
		case 1:
			// TODO: merge the default and this case.
			if (waypoints.currentIndex() >= 1) {
				if (!waypoints.isStarted()) {
					waypoints.schedule(npc, 0);
				}
				if (!npc.isPaused() && npc.getHandle().pathFinished()) {
					waypoints.setIndex(0);
					waypoints.setStarted(false);
				}
			} else {
				if (!npc.getWaypoints().isStarted()) {
					PathUtils.createPath(npc, npc.getNPCData().getLocation(),
							-1, -1,
							SettingsManager.getDouble("PathfindingRange"));
					waypoints.setStarted(true);
				}
				if (!npc.isPaused() && npc.getHandle().pathFinished()) {
					waypoints.setIndex(1);
					waypoints.setStarted(false);
				}
			}
			break;
		default:
			if (!waypoints.isStarted()) {
				if (waypoints.currentIndex() + 1 > waypoints.size()) {
					waypoints.setIndex(0);
				}
				waypoints.scheduleNext(npc);
			}
			if (!npc.isPaused() && npc.getHandle().pathFinished()) {
				waypoints.setIndex(waypoints.currentIndex() + 1);
				waypoints.setStarted(false);
			}
		}
	}

	private void resetActions(int entityID, String name, HumanNPC npc) {
		ActionManager.resetAction(entityID, name, "saidText", npc.getNPCData()
				.isTalkClose());
	}

	private void cacheActions(Player player, HumanNPC npc, int UID, String name) {
		CachedAction cached = ActionManager.getAction(UID, name);
		if (!cached.has("saidText") && npc.getNPCData().isTalkClose()) {
			MessageUtils.sendText(npc, player);
			cached.set("saidText");
		}
		ActionManager.putAction(UID, name, cached);
	}

	public static void scheduleRespawn(HumanNPC npc, int delay) {
		NPCManager.removeForRespawn(npc.getUID());
		new RespawnTask(npc).register(delay);
	}

	public static class RespawnTask implements Runnable {
		private final int UID;
		private final String owner;

		public RespawnTask(HumanNPC npc) {
			this.UID = npc.getUID();
			this.owner = npc.getOwner();
		}

		public void register(int delay) {
			Bukkit.getServer().getScheduler()
					.scheduleSyncDelayedTask(Citizens.plugin, this, delay);
		}

		@Override
		public void run() {
			NPCManager.register(UID, owner, NPCCreateReason.RESPAWN);
			Messaging.sendUncertain(owner,
					StringUtils.wrap(NPCManager.get(UID).getName())
							+ " has respawned.");
		}
	}
}