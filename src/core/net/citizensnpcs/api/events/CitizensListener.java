package net.citizensnpcs.api.events;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

public class CitizensListener extends CustomEventListener {

	/**
	 * Called when Citizens is enabled, after all NPC types and settings have
	 * been loaded
	 */
	public void onCitizensEnable(CitizensEnableEvent event) {
	}

	/**
	 * Called when the command /citizens reload is used in-game
	 */
	public void onCitizensReload(CitizensReloadEvent event) {
	}

	@Override
	public void onCustomEvent(Event event) {
		if (event instanceof CitizensEnableEvent) {
			onCitizensEnable((CitizensEnableEvent) event);
		} else if (event instanceof CitizensReloadEvent) {
			onCitizensReload((CitizensReloadEvent) event);
		}
	}
}