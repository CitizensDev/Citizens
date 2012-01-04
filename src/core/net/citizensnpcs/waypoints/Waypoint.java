package net.citizensnpcs.waypoints;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.lib.HumanNPC;

import org.bukkit.Location;

public class Waypoint {
	private final Location waypoint;
	private final List<WaypointModifier> modifiers = new ArrayList<WaypointModifier>();
	private int delay;

	public Waypoint(Location waypoint) {
		this.waypoint = waypoint;
	}

	public void addModifier(WaypointModifier effect) {
		this.modifiers.add(effect);
	}

	public int getDelay() {
		return delay;
	}

	public Location getLocation() {
		return waypoint;
	}

	public List<WaypointModifier> getModifiers() {
		return this.modifiers;
	}

	public void onReach(HumanNPC npc) {
		for (WaypointModifier modifier : modifiers) {
			modifier.onReach(npc);
		}
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
}