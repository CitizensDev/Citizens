package com.citizens.resources.redecouverte.NPClib;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;

import com.citizens.Utils.Messaging;

@SuppressWarnings("serial")
public class NPCList extends ConcurrentHashMap<Integer, HumanNPC> {

	public boolean containsBukkitEntity(Entity entity) {
		return getNPC(entity) != null;
	}

	public HumanNPC getNPC(Entity entity) {
		if (entity == null) {
			return null;
		}
		for (HumanNPC bnpc : this.values()) {
			if (bnpc == null || bnpc.getPlayer() == null) {
				if (bnpc == null) {
					Messaging.log("Null NPC found!");
				} else if (bnpc.getPlayer() == null) {
					Messaging.log("Craftbukkit entity was null!");
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