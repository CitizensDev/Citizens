package net.citizensnpcs.resources.npclib;

import net.minecraft.server.PathEntity;

public interface AutoPathfinder {
	PathEntity find(PathNPC npc);
}
