package net.citizensnpcs.api.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;

public class NPCTargetEvent extends EntityTargetEvent {
	public NPCTargetEvent(Entity entity, Entity target) {
		super(entity, target, TargetReason.CUSTOM);
	}

	private static final long serialVersionUID = 1L;
}