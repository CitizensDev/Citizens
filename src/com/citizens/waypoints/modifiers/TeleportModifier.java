package com.citizens.waypoints.modifiers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.citizens.interfaces.Storage;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.ConversationUtils.ChatType;
import com.citizens.utils.ConversationUtils.ConversationMessage;
import com.citizens.utils.LocationUtils;
import com.citizens.waypoints.Waypoint;
import com.citizens.waypoints.WaypointModifier;
import com.citizens.waypoints.WaypointModifierType;

public class TeleportModifier implements WaypointModifier {
	private Location loc;

	@Override
	public void begin(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean converse(ConversationMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean special(ChatType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void end(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReach(HumanNPC npc, Waypoint waypoint) {
		npc.teleport(loc);
	}

	@Override
	public WaypointModifierType getType() {
		return WaypointModifierType.TELEPORT;
	}

	@Override
	public void parse(Storage storage, String root) {
		loc = LocationUtils.loadLocation(storage, root);
	}

	@Override
	public void save(Storage storage, String root) {
		LocationUtils.saveLocation(storage, loc, root);
	}
}
