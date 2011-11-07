package net.citizensnpcs.npcdata;

import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.waypoints.Waypoint;
import net.citizensnpcs.waypoints.WaypointPath;

public class PathEditingSession {
	private int index = 0;
	private final int UID;

	public PathEditingSession(int UID, int index) {
		this.UID = UID;
		this.index = index;
	}

	public HumanNPC getNPC() {
		return NPCManager.get(UID);
	}

	public int getUID() {
		return this.UID;
	}

	public void insert(WaypointPath waypoints, Waypoint waypoint) {
		waypoints.insert(waypoint, index++);
	}

	public int getIndex() {
		return this.index;
	}

	public void remove(WaypointPath waypoints) {
		waypoints.remove(index--);
	}

	public void restartAtIndex() {
		WaypointPath path = getNPC().getWaypoints();
		if (path.size() == 0)
			return;
		path.setIndex(index);
		getNPC().teleport(path.current().getLocation());
	}
}
