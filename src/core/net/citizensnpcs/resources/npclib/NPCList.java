package net.citizensnpcs.resources.npclib;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.craftbukkit.v1_5_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class NPCList extends ConcurrentHashMap<Integer, HumanNPC> {
    public boolean containsBukkitEntity(Entity entity) {
        return getNPC(entity) != null;
    }

    public HumanNPC getNPC(Entity entity) {
        if (entity == null) {
            return null;
        }
        net.minecraft.server.v1_5_R2.Entity mcEntity = ((CraftEntity) entity).getHandle();
        if (mcEntity instanceof CraftNPC) {
            HumanNPC npc = ((CraftNPC) mcEntity).npc;
            if (npc == null)
                return null;
            // Compare object references to eliminate conflicting UIDs.
            if (get(npc.getUID()) == npc) {
                return npc;
            }
        }
        return null;
    }

    private static final long serialVersionUID = 7208318521278059987L;
}