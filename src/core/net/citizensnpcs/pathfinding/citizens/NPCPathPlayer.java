package net.citizensnpcs.pathfinding.citizens;

import net.citizensnpcs.pathfinding.PathFinder.PathPlayer;
import net.citizensnpcs.resources.npclib.HumanNPC;

public final class NPCPathPlayer implements PathPlayer {
	private final HumanNPC npc;

	public NPCPathPlayer(HumanNPC npc) {
		this.npc = npc;
	}

	public HumanNPC getNPC() {
		return npc;
	}
}