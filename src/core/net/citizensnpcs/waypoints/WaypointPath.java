package net.citizensnpcs.waypoints;

import net.citizensnpcs.misc.StateHolder;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface WaypointPath extends StateHolder {
	public void addWaypoint(Location location);

	public void clear();

	public PathEditor createEditor(Player player);

	public void restart();

	public void delay(int ticks);
}
