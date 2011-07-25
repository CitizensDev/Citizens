package com.citizens.waypoints;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.citizens.Citizens;
import com.citizens.SettingsManager.Constant;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.PathUtils;

public class WaypointPath {
	private List<Waypoint> waypoints = new ArrayList<Waypoint>();
	private boolean waypointStarted = false;
	private int waypointIndex = 0;

	public void resetWaypoints() {
		setPoints(new ArrayList<Waypoint>());
		this.waypointIndex = 0;
	}

	public void setIndex(int waypointIndex) {
		this.waypointIndex = waypointIndex;
	}

	public Waypoint current() {
		if (waypoints.size() > waypointIndex) {
			return this.get(waypointIndex);
		}
		return null;
	}

	public int currentIndex() {
		return waypointIndex;
	}

	public void setStarted(boolean waypointStarted) {
		this.waypointStarted = waypointStarted;
	}

	public boolean isStarted() {
		return waypointStarted;
	}

	public List<Waypoint> getWaypoints() {
		return this.waypoints;
	}

	public void setPoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

	public Waypoint get(int index) {
		return this.waypoints.get(index);
	}

	public void add(Waypoint location) {
		this.waypoints.add(location);
	}

	public void removeLast() {
		this.waypoints.remove(this.waypoints.size() - 1);
	}

	public int size() {
		return waypoints.size();
	}

	public Waypoint getLast() {
		if (waypoints.size() == 0) {
			return null;
		}
		return waypoints.get(waypoints.size() - 1);
	}

	public void scheduleNext(HumanNPC npc) {
		schedule(npc, currentIndex());

	}

	public void schedule(HumanNPC npc, int index) {
		this.setStarted(true);
		WaypointScheduler scheduler = new WaypointScheduler(npc, waypoints.get(
				index).getLocation());
		if (waypoints.get(index).getDelay() > 0) {
			Bukkit.getServer()
					.getScheduler()
					.scheduleSyncDelayedTask(Citizens.plugin, scheduler,
							waypoints.get(index).getDelay());
		} else {
			scheduler.run();
		}

	}

	public void scheduleDelay(HumanNPC npc, Location target, int delay) {
		RestartPathTask task = new RestartPathTask(npc, target);
		if (delay > 0) {
			Bukkit.getServer()
					.getScheduler()
					.scheduleSyncDelayedTask(Citizens.plugin,
							new RestartPathTask(npc, target), delay);
		} else {
			task.run();
		}
		npc.setPaused(true);
	}

	private static class WaypointScheduler implements Runnable {
		private final HumanNPC npc;
		private final Location target;

		public WaypointScheduler(HumanNPC npc, Location target) {
			this.npc = npc;
			this.target = target;
		}

		@Override
		public void run() {
			PathUtils.createPath(npc, target, -1, -1,
					Constant.PathfindingRange.toDouble());
		}
	}

	private static class RestartPathTask implements Runnable {
		private final Location point;
		private final HumanNPC npc;

		public RestartPathTask(HumanNPC npc, Location point) {
			this.npc = npc;
			this.point = point;
		}

		@Override
		public void run() {
			PathUtils.createPath(npc, point, -1, -1,
					Constant.PathfindingRange.toDouble());
			npc.setPaused(false);
		}
	}
}
