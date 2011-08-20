package net.citizensnpcs.listeners;

import net.citizensnpcs.Citizens;

public interface Listener {
	/**
	 * Register the events for an NPC type
	 * 
	 * @param plugin
	 *            Citizens object
	 */
	public void registerEvents(Citizens plugin);
}