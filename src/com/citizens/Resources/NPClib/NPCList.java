package com.citizens.Resources.NPClib;

import java.util.concurrent.ConcurrentHashMap;

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
		if (((CraftEntity) entity).getHandle() instanceof CraftNPC) {
			return ((CraftNPC) (((CraftEntity) entity).getHandle())).npc;
		}
		return null;
	}
}