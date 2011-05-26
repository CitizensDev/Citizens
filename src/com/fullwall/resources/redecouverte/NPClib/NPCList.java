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
		if (entity == null) {
			return null;
		}
		for (HumanNPC bnpc : this.values()) {
			if (bnpc == null || bnpc.getPlayer() == null) {
				if (bnpc == null) {
					Citizens.log.info("[Citizens]: Null NPC found!");
				} else if (bnpc.getPlayer() == null) {
					Citizens.log
							.info("[Citizens]: Craftbukkit entity of NPC was null!");
				}
				continue;
			}
			if (bnpc.getPlayer().getEntityId() == entity.getEntityId()) {
				return bnpc;
			}
		}
		return null;
	}
}