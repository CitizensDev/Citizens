package net.citizensnpcs.waypoints;

import net.citizensnpcs.misc.StateHolder;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface WaypointPath0 extends StateHolder {
	public void addWaypoint(Location location);

	public void clear();

	public PathEditor newEditorSession(Player player);

	public void restartPath();

	public void startPath();
}
