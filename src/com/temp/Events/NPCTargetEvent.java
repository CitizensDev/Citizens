package com.temp.Events;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;

public class NPCTargetEvent extends EntityTargetEvent {
	private static final long serialVersionUID = 1L;

	public NPCTargetEvent(Entity entity, Entity target) {
		super(entity, target, TargetReason.CUSTOM);
	}
}