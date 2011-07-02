package com.citizens.Pathfinding.Citizens;

import com.citizens.Pathfinding.PathFinder.PathPlayer;
import com.citizens.Resources.NPClib.HumanNPC;

public final class NPCPathPlayer implements PathPlayer {
	private final HumanNPC npc;

	public NPCPathPlayer(HumanNPC npc) {
		this.npc = npc;
	}

	public HumanNPC getNPC() {
		return npc;
	}
}