package com.citizens.waypoints;

import com.citizens.interfaces.Storage;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.ConversationUtils.Converser;

public interface WaypointModifier extends Converser {
	public void onReach(HumanNPC npc);

	public void parse(Storage storage, String root);

	public void save(Storage storage, String root);

	public WaypointModifierType getType();
}
