package net.citizensnpcs.api.events;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

public class NPCListener extends CustomEventListener {

	public void onNPCSpawn(NPCSpawnEvent event) {
	}

	public void onNPCDisplayText(NPCTalkEvent event) {
	}

	public void onNPCInventoryOpen(NPCInventoryOpenEvent event) {
	}

	public void onNPCRightClick(NPCRightClickEvent event) {
	}

	public void onNPCCreate(NPCCreateEvent event) {
	}

	public void onNPCTarget(NPCTargetEvent event) {
	}

	public void onNPCRemove(NPCRemoveEvent event) {
	}

	@Override
	public void onCustomEvent(Event event) {
		if (event instanceof NPCSpawnEvent) {
			onNPCSpawn((NPCSpawnEvent) event);
		} else if (event instanceof NPCTalkEvent) {
			onNPCDisplayText((NPCTalkEvent) event);
		} else if (event instanceof NPCInventoryOpenEvent) {
			onNPCInventoryOpen((NPCInventoryOpenEvent) event);
		} else if (event instanceof NPCRightClickEvent) {
			onNPCRightClick((NPCRightClickEvent) event);
		} else if (event instanceof NPCCreateEvent) {
			onNPCCreate((NPCCreateEvent) event);
		} else if (event instanceof NPCTargetEvent) {
			onNPCTarget((NPCTargetEvent) event);
		} else if (event instanceof NPCRemoveEvent) {
			onNPCRemove((NPCRemoveEvent) event);
		}
	}
}