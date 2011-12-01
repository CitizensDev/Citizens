package net.citizensnpcs.waypoints;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Location;

public interface WaypointPath0 {
	public void addWaypoint(Location location);

	public void clear();

	public Waypoint getCurrentWaypoint();

	public void startPath(HumanNPC npc);
}
