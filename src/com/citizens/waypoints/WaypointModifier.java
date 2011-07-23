package com.citizens.waypoints;

import com.citizens.interfaces.Storage;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.ConversationUtils.Converser;

public abstract class WaypointModifier extends Converser {
	protected Waypoint waypoint;

	public WaypointModifier(Waypoint waypoint) {
		this.waypoint = waypoint;
	}

	public abstract void onReach(HumanNPC npc);

	public abstract void parse(Storage storage, String root);

	public abstract void save(Storage storage, String root);

	public abstract WaypointModifierType getType();
}