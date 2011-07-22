package com.citizens.waypoints;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class WaypointPath {
	private List<Location> waypoints = new ArrayList<Location>();
	private boolean waypointStarted = false;
	private int waypointIndex = 0;

	public void resetWaypoints() {
		setPoints(new ArrayList<Location>());
		this.waypointIndex = 0;
	}

	public void setIndex(int waypointIndex) {
		this.waypointIndex = waypointIndex;
	}

	public Location current() {
		if (waypoints.size() - 1 > waypointIndex)
			return this.get(waypointIndex);
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

	public List<Location> getWaypoints() {
		return this.waypoints;
	}

	public void setPoints(List<Location> waypoints) {
		this.waypoints = waypoints;
	}

	public Location get(int index) {
		return this.getWaypoints().get(index);
	}

	public void add(Location location) {
		this.getWaypoints().add(location);
	}

	public void removeLast() {
		this.getWaypoints().remove(this.getWaypoints().size() - 1);
	}

	public int size() {
		return getWaypoints().size();
	}

	public Location getLast() {
		if (waypoints.size() == 0)
			return null;
		return waypoints.get(waypoints.size() - 1);
	}
}