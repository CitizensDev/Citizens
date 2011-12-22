package net.citizensnpcs.lib;

import net.minecraft.server.PathEntity;

public interface AutoPathfinder {
	PathEntity find(PathNPC npc);
}
