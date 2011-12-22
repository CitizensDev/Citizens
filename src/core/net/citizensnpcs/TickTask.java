package net.citizensnpcs;

import java.util.Map;

import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.lib.NPCSpawner;
import net.citizensnpcs.misc.Actions;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.PathUtils;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.waypoints.WaypointPath;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

public class TickTask implements Runnable {
	private static final Map<HumanNPC, Actions> cachedActions = Maps
			.newHashMap();

	@Override
	public void run() {
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		for (HumanNPC npc : NPCManager.getNPCs()) {
			updateWaypoints(npc);
			npc.doTick();
			NPCSpawner.removeNPCFromPlayerList(npc);
			if (!npc.getNPCData().isLookClose()
					&& !npc.getNPCData().isTalkClose())
				continue;
			for (Player player : online) {
				// If the player is within 'seeing' range
				if (LocationUtils.withinRange(npc.getLocation(),
						player.getLocation(), Settings.getDouble("NPCRange"))) {
					if (npc.getHandle().pathFinished()
							&& !npc.getHandle().hasTarget()
							&& npc.getNPCData().isLookClose()) {
						NPCManager.faceEntity(npc.getPlayer(), player);
					}
					cacheActions(npc, player);
				} else {
					clearActions(npc, player);
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
				if (waypoints.isStarted() && !npc.isPaused()
						&& npc.getHandle().pathFinished()) {
					waypoints.setIndex(0);
				}
			} else {
				if (!npc.getWaypoints().isStarted()) {
					PathUtils.createPath(npc, npc.getNPCData().getLocation(),
							-1, -1, Settings.getDouble("PathfindingRange"));
					waypoints.setStarted(true);
				}
				if (waypoints.isStarted() && !npc.isPaused()
						&& npc.getHandle().pathFinished()) {
					waypoints.setIndex(1);
				}
			}
			if (waypoints.isStarted() && !npc.isPaused()
					&& npc.getHandle().pathFinished()) {
				waypoints.setStarted(false);
				waypoints.onReach(npc);
			}
			break;
		default:
			if (!waypoints.isStarted()) {
				waypoints.scheduleNext(npc);
			}
			if (waypoints.isStarted() && !npc.isPaused()
					&& npc.getHandle().pathFinished()) {
				waypoints.setIndex(waypoints.currentIndex() + 1);
				waypoints.setStarted(false);
				waypoints.onReach(npc);
			}
		}
	}

	private static void clearActions(HumanNPC npc, Player player) {
		Actions actions = cachedActions.get(npc);
		if (actions == null) {
			cachedActions.put(npc, new Actions());
			return;
		}
		actions.clear("saidText", player.getName());
	}

	private static void cacheActions(HumanNPC npc, Player player) {
		Actions actions = cachedActions.get(npc);
		if (actions == null) {
			cachedActions.put(npc, new Actions());
			return;
		}
		if (!actions.has("saidText", player.getName())
				&& npc.getNPCData().isTalkClose()) {
			MessageUtils.sendText(npc, player);
			actions.set("saidText", player.getName());
		}
	}

	public static void scheduleRespawn(HumanNPC npc, int delay) {
		npc.despawn();
		new RespawnTask(npc).register(delay);
	}

	public static class RespawnTask implements Runnable {
		private final HumanNPC npc;
		private final String owner;

		public RespawnTask(HumanNPC npc) {
			this.npc = npc;
			this.owner = npc.getOwner();
		}

		public void register(int delay) {
			Bukkit.getServer().getScheduler()
					.scheduleSyncDelayedTask(Citizens.plugin, this, delay);
		}

		@Override
		public void run() {
			npc.spawn();
			Messaging.sendUncertain(owner, StringUtils.wrap(npc.getName())
					+ " has respawned.");
		}
	}

	public static void clearActions(Player player) {
		for (HumanNPC npc : cachedActions.keySet()) {
			clearActions(npc, player);
		}
	}
}