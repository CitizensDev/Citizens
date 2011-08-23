package net.citizensnpcs.api.events;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

public class NPCListener extends CustomEventListener {

	/**
	 * Called when an NPC says a line or lines from its text
	 */
	public void onNPCTalk(NPCTalkEvent event) {
	}

	/**
	 * Called when an NPC's Inventory, deprecating this when Bukkit adds
	 * inventory events
	 */
	public void onNPCInventoryOpen(NPCInventoryOpenEvent event) {
	}

	/**
	 * Called when an NPC is created in the world by any means
	 */
	public void onNPCCreate(NPCCreateEvent event) {
	}

	/**
	 * Called when a NPC is targeted
	 */
	public void onNPCTarget(NPCTargetEvent event) {
	}

	/**
	 * Called when an NPC is removed from the world by any means
	 */
	public void onNPCRemove(NPCRemoveEvent event) {
	}

	/**
	 * Called when an NPC is right-clicked
	 */
	public void onNPCRightClick(NPCRightClickEvent event) {

	}

	@Override
	public void onCustomEvent(Event event) {
		if (event instanceof NPCTalkEvent) {
			onNPCTalk((NPCTalkEvent) event);
		} else if (event instanceof NPCInventoryOpenEvent) {
			onNPCInventoryOpen((NPCInventoryOpenEvent) event);
		} else if (event instanceof NPCCreateEvent) {
			onNPCCreate((NPCCreateEvent) event);
		} else if (event instanceof NPCTargetEvent) {
			onNPCTarget((NPCTargetEvent) event);
		} else if (event instanceof NPCRemoveEvent) {
			onNPCRemove((NPCRemoveEvent) event);
		} else if (event instanceof NPCRightClickEvent) {
			onNPCRightClick((NPCRightClickEvent) event);
		}
	}
}