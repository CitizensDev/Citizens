package net.citizensnpcs.waypoints;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.PathUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class WaypointPath {
	private List<Waypoint> points = new ArrayList<Waypoint>();
	private boolean started = false;
	private int index = 0;

	public void resetWaypoints() {
		setPoints(new ArrayList<Waypoint>());
		this.index = 0;
	}

	public void setIndex(int waypointIndex) {
		this.index = waypointIndex;
	}

	public Waypoint current() {
		if (points.size() > index) {
			return this.get(index);
		}
		return null;
	}

	public int currentIndex() {
		return index;
	}

	public void setStarted(boolean waypointStarted) {
		this.started = waypointStarted;
	}

	public boolean isStarted() {
		return started;
	}

	public List<Waypoint> getWaypoints() {
		return this.points;
	}

	public void setPoints(List<Waypoint> waypoints) {
		this.points = waypoints;
	}

	public Waypoint get(int index) {
		return this.points.get(index);
	}

	public void add(Waypoint location) {
		this.points.add(location);
	}

	public void removeLast() {
		this.points.remove(this.points.size() - 1);
	}

	public int size() {
		return points.size();
	}

	public Waypoint getLast() {
		if (points.size() == 0) {
			return null;
		}
		return points.get(points.size() - 1);
	}

	private void onReach(HumanNPC npc) {
		if (current() != null)
			current().onReach(npc);
	}

	public void scheduleNext(HumanNPC npc) {
		schedule(npc, currentIndex());
	}

	public void schedule(HumanNPC npc, int index) {
		onReach(npc);
		this.setStarted(true);
		WaypointScheduler scheduler = new WaypointScheduler(npc, points.get(
				index).getLocation());
		if (points.get(index).getDelay() > 0) {
			Bukkit.getServer()
					.getScheduler()
					.scheduleSyncDelayedTask(Citizens.plugin, scheduler,
							points.get(index).getDelay());
		} else {
			scheduler.run();
		}
	}

	public void scheduleDelay(HumanNPC npc, Location target, int delay) {
		onReach(npc);
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
					SettingsManager.getDouble("PathfindingRange"));
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
					SettingsManager.getDouble("PathfindingRange"));
			npc.setPaused(false);
		}
	}
}