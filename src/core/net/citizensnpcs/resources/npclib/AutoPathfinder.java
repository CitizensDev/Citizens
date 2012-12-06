package net.citizensnpcs.resources.npclib;

import net.minecraft.server.v1_4_5.PathEntity;

public interface AutoPathfinder {
    PathEntity find(PathNPC npc);
}
