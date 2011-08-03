package net.citizensnpcs.resources.npclib;

import java.util.concurrent.ConcurrentHashMap;

import net.citizensnpcs.resources.npclib.creatures.CreatureNPC;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class NPCList extends ConcurrentHashMap<Integer, HumanNPC> {

	private static final long serialVersionUID = 7208318521278059987L;

	public boolean containsBukkitEntity(Entity entity) {
		return getNPC(entity) != null;
	}

	public HumanNPC getNPC(Entity entity) {
		if (entity == null) {
			return null;
		}
		net.minecraft.server.Entity mcEntity = ((CraftEntity) entity)
				.getHandle();
		if (mcEntity instanceof CraftNPC && !(mcEntity instanceof CreatureNPC)) {
			HumanNPC npc = ((CraftNPC) mcEntity).npc;
			if (get(npc.getUID()) != null)
				return npc;
		}
		return null;
	}
}