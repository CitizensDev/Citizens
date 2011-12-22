package net.citizensnpcs.waypoints;

import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.misc.StateHolder;
import net.citizensnpcs.utils.ConversationUtils.Converser;

public abstract class WaypointModifier extends Converser implements StateHolder {
	protected Waypoint waypoint;

	public WaypointModifier(Waypoint waypoint) {
		this.waypoint = waypoint;
	}

	public abstract void onReach(HumanNPC npc);

	public abstract WaypointModifierType getType();
}