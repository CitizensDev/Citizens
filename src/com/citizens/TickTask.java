package com.citizens;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.citizens.Misc.ActionManager;
import com.citizens.Misc.CachedAction;
import com.citizens.NPCs.NPCManager;
import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Resources.NPClib.NPCSpawner;
import com.citizens.Resources.NPClib.WaypointPath;
import com.citizens.Utils.LocationUtils;
import com.citizens.Utils.MessageUtils;
import com.citizens.Utils.Messaging;
import com.citizens.Utils.PathUtils;
import com.citizens.Utils.StringUtils;

public class TickTask implements Runnable {
	// How far an NPC can 'see'
	private final double range;

	public TickTask(double range) {
		// range is checked in both directions, so we halve the passed range.
		this.range = range / 2;
	}

	@Override
	public void run() {
		HumanNPC npc;
		int UID;
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			{
				npc = entry.getValue();
				updateWaypoints(npc);
				npc.updateMovement();
				NPCSpawner.removeNPCFromPlayerList(npc);
				if (npc.getPlayer().isDead()) {
					NPCSpawner.despawnNPC(npc);
				}
				UID = entry.getKey();
				for (Player p : online) {
					String name = p.getName();
					if (npc.getNPCData().isLookClose()
							|| npc.getNPCData().isTalkClose()) {
						// If the player is within 'seeing' range
						if (LocationUtils.checkLocation(npc.getLocation(),
								p.getLocation(), range)) {
							if (npc.getHandle().pathFinished()
									&& npc.getNPCData().isLookClose()) {
								NPCManager.facePlayer(npc, p);
							}
							cacheActions(p, npc, UID, name);
						} else {
							resetActions(UID, name, npc);
						}
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
					PathUtils.createPath(npc, waypoints.get(0), -1, -1,
							Constants.pathFindingRange);
					waypoints.setStarted(true);
				}
				if (!npc.isPaused() && npc.getHandle().pathFinished()) {
					waypoints.setIndex(0);
					waypoints.setStarted(false);
				}
			} else {
				if (!npc.getWaypoints().isStarted()) {
					PathUtils.createPath(npc, npc.getNPCData().getLocation(),
							-1, -1, Constants.pathFindingRange);
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
				PathUtils.createPath(npc, waypoints.current(), -1, -1,
						Constants.pathFindingRange);
				waypoints.setStarted(true);
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

	private void cacheActions(Player p, HumanNPC npc, int entityID, String name) {
		CachedAction cached = ActionManager.getAction(entityID, name);
		if (!cached.has("saidText") && npc.getNPCData().isTalkClose()) {
			MessageUtils.sendText(npc, p);
			cached.set("saidText");
		}
		ActionManager.putAction(entityID, name, cached);
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
			NPCManager.register(UID, owner);
			Messaging.sendUncertain(owner,
					StringUtils.wrap(NPCManager.get(UID).getName())
							+ " has respawned.");
		}
	}
}