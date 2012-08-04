package net.citizensnpcs.waypoints;

import org.bukkit.Location;

public interface LinearWaypointPath0 extends WaypointPath0 {
	public Waypoint getFromIndex(int index) throws IllegalIndexException;

	public void insert(Location location, int index);

	public void removeLast();

	public void setIndex() throws IllegalIndexException;

	public int size();

	public static class IllegalIndexException extends Exception {
		private static final long serialVersionUID = 1L;
	}
}
