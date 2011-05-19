package com.fullwall.resources.redecouverte.NPClib;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;

import com.fullwall.Citizens.Citizens;

@SuppressWarnings("serial")
public class NPCList extends ConcurrentHashMap<Integer, HumanNPC> {

	public boolean containsBukkitEntity(Entity entity) {
		return getBasicHumanNpc(entity) != null;
	}

	public HumanNPC getBasicHumanNpc(Entity entity) {
		for (HumanNPC bnpc : this.values()) {
			if (bnpc == null) {
				Citizens.log.info("[Citizens]: Null NPC found!");
				continue;
			}
			if (bnpc.getPlayer().getEntityId() == entity.getEntityId()) {
				return bnpc;
			}
		}
		return null;
	}
}