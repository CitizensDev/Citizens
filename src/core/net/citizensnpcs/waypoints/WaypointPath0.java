package net.citizensnpcs.waypoints;

import net.citizensnpcs.resources.npclib.HumanNPC;

public interface WaypointPath0 {
	public void removeLast();

	public int size();

	public void startPath(HumanNPC npc);

	public Waypoint getCurrentWaypoint();
}
