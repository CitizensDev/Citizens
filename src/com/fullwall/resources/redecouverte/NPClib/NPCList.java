package com.fullwall.resources.redecouverte.NPClib;

import java.util.HashMap;
import org.bukkit.entity.Entity;

@SuppressWarnings("serial")
public class NPCList extends HashMap<Integer, HumanNPC> {

	public boolean containsBukkitEntity(Entity entity) {
		for (HumanNPC bnpc : this.values()) {
			if (bnpc.getPlayer().getEntityId() == entity.getEntityId())
				return true;
		}

		return false;
	}

	public HumanNPC getBasicHumanNpc(Entity entity) {
		for (HumanNPC bnpc : this.values()) {
			if (bnpc.getPlayer().getEntityId() == entity.getEntityId())
				return bnpc;
		}
		return null;
	}

}
