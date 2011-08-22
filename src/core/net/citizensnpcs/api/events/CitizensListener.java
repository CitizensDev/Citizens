package net.citizensnpcs.api.events;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

public class CitizensListener extends CustomEventListener {

	/**
	 * Called when Citizens is enabled, after all NPC types and settings have
	 * been loaded
	 * 
	 * @param event
	 */
	public void onCitizensEnable(CitizensEnableEvent event) {
	}

	@Override
	public void onCustomEvent(Event event) {
		if (event instanceof CitizensEnableEvent) {
			onCitizensEnable((CitizensEnableEvent) event);
		}
	}
}