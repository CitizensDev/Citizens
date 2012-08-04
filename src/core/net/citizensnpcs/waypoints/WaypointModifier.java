package net.citizensnpcs.waypoints;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.ConversationUtils.Converser;

public abstract class WaypointModifier extends Converser {
	protected Waypoint waypoint;

	public WaypointModifier(Waypoint waypoint) {
		this.waypoint = waypoint;
	}

	public abstract WaypointModifierType getType();

	public abstract void onReach(HumanNPC npc);

	public abstract void parse(Storage storage, String root);

	public abstract void save(Storage storage, String root);
}