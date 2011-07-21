package com.citizens.waypoints;

import org.bukkit.entity.Player;

import com.citizens.interfaces.Storage;
import com.citizens.resources.npclib.HumanNPC;

public interface WaypointModifier {
	public boolean construct(Player player, String message);

	public void onReach(HumanNPC npc);

	public void parse(Storage storage, String root);

	public void save(Storage storage, String root);

	public WaypointModifierType getType();
}
