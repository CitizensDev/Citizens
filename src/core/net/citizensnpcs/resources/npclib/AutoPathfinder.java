package net.citizensnpcs.resources.npclib;

import net.minecraft.server.v1_6_R3.PathEntity;

public interface AutoPathfinder {
    PathEntity find(PathNPC npc);
}
