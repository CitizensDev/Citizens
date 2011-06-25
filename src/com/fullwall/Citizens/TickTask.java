package com.fullwall.Citizens;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Misc.ActionManager;
import com.fullwall.Citizens.Misc.CachedAction;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.LocationUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCSpawner;
import com.fullwall.resources.redecouverte.NPClib.WaypointPath;

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
				UID = entry.getKey();
				for (Player p : online) {
					String name = p.getName();
					if (npc.getNPCData().isLookClose()
							|| npc.getNPCData().isTalkClose()) {
						// If the player is within 'seeing' range
						if (LocationUtils.checkLocation(npc.getLocation(),
								p.getLocation(), range)) {
							if (npc.getNPCData().isLookClose()) {
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
		if (NPCManager.pathEditors.get(npc.getOwner()) == null) {
			WaypointPath waypoints = npc.getWaypoints();
			switch (waypoints.size()) {
			case 0:
				break;
			case 1:
				// TODO: merge the default and this case.
				if (waypoints.getIndex() >= 1) {
					if (!waypoints.isStarted()) {
						npc.createPath(waypoints.get(0), -1, -1,
								Constants.pathFindingRange);
						waypoints.setStarted(true);
					}
					if (!npc.paused() && npc.getHandle().pathFinished()) {
						waypoints.setIndex(0);
						waypoints.setStarted(false);
					}
				} else {
					if (!npc.getWaypoints().isStarted()) {
						npc.createPath(npc.getNPCData().getLocation(), -1, -1,
								Constants.pathFindingRange);
						waypoints.setStarted(true);
					}
					if (!npc.paused() && npc.getHandle().pathFinished()) {
						waypoints.setIndex(1);
						waypoints.setStarted(false);
					}
				}
				break;
			default:
				if (!waypoints.isStarted()) {
					if (waypoints.getIndex() + 1 > waypoints.size()) {
						waypoints.setIndex(0);
					}
					npc.createPath(waypoints.get(waypoints.getIndex()), -1, -1,
							Constants.pathFindingRange);
					waypoints.setStarted(true);
				}
				if (!npc.paused() && npc.getHandle().pathFinished()) {
					waypoints.setIndex(waypoints.getIndex() + 1);
					waypoints.setStarted(false);
				}
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
}