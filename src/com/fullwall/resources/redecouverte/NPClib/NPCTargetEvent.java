package com.fullwall.resources.redecouverte.NPClib;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;

public class NPCTargetEvent extends EntityTargetEvent {

	public static enum NPCTargetReason {
		CLOSEST_PLAYER, NPC_RIGHTCLICKED, NPC_BOUNCED
	}

	private static final long serialVersionUID = 1L;
	private final NPCTargetReason reason;

	public NPCTargetEvent(Entity entity, Entity target, NPCTargetReason reason) {
		super(entity, target, TargetReason.CUSTOM);
		this.reason = reason;
	}

	public NPCTargetReason getNPCTargetReason() {
		return this.reason;
	}
}