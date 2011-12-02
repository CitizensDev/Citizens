package net.citizensnpcs.waypoints;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface WaypointPath0 {
	public void addWaypoint(Location location);

	public void clear();

	public PathEditor newEditorSession(Player player);

	public void restartPath();

	public void startPath();
}
