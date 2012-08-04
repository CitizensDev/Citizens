package net.citizensnpcs.waypoints;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.PathUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class WaypointPath {
	private int index = 0;
	private final List<Waypoint> points = new ArrayList<Waypoint>();
	private boolean started = false;

	public Waypoint current() {
		if (points.size() == 0)
			return null;
		return this.get(currentIndex());
	}

	public int currentIndex() {
		if (index >= points.size())
			index = 0;
		return index;
	}

	public Waypoint get(int index) {
		return this.points.get(index);
	}

	public Waypoint getLast() {
		if (points.size() == 0) {
			return null;
		}
		return points.get(points.size() - 1);
	}

	public List<Waypoint> getWaypoints() {
		return this.points;
	}

	public void insert(Waypoint location, int index) {
		this.points.add(index, location);
	}

	public boolean isStarted() {
		return started;
	}

	public void onReach(HumanNPC npc) {
		if (current() != null)
			current().onReach(npc);
	}

	public void remove(int index) {
		this.points.remove(index);
	}

	public void resetWaypoints() {
		this.points.clear();
		this.index = 0;
	}

	public void schedule(HumanNPC npc, int index) {
		this.setStarted(true);
		WaypointScheduler scheduler = new WaypointScheduler(npc, points.get(
				index).getLocation());
		if (points.get(index).getDelay() > 0) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin,
					scheduler, points.get(index).getDelay());
		} else {
			scheduler.run();
		}
	}

	public void scheduleDelay(HumanNPC npc, Location target, int delay) {
		RestartPathTask task = new RestartPathTask(npc, target);
		npc.setPaused(true);
		if (delay > 0) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin,
					new RestartPathTask(npc, target), delay);
		} else {
			task.run();
		}
	}

	public void scheduleNext(HumanNPC npc) {
		schedule(npc, currentIndex());
	}

	public void setIndex(int waypointIndex) {
		this.index = waypointIndex;
	}

	public void setPoints(List<Waypoint> waypoints) {
		this.points.clear();
		this.points.addAll(waypoints);
	}

	public void setStarted(boolean waypointStarted) {
		this.started = waypointStarted;
	}

	public int size() {
		return points.size();
	}

	private static class RestartPathTask implements Runnable {
		private final HumanNPC npc;
		private final Location point;

		public RestartPathTask(HumanNPC npc, Location point) {
			this.npc = npc;
			this.point = point;
		}

		@Override
		public void run() {
			PathUtils.createPath(npc, point, -1, -1,
					Settings.getDouble("PathfindingRange"));
			npc.setPaused(false);
		}
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
					Settings.getDouble("PathfindingRange"));
		}
	}
}