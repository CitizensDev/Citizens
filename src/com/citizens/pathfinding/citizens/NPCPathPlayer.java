package com.citizens.pathfinding.citizens;

import com.citizens.pathfinding.PathFinder.PathPlayer;
import com.citizens.resources.npclib.HumanNPC;

public final class NPCPathPlayer implements PathPlayer {
	private final HumanNPC npc;

	public NPCPathPlayer(HumanNPC npc) {
		this.npc = npc;
	}

	public HumanNPC getNPC() {
		return npc;
	}
}