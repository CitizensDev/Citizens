package com.fullwall.Citizens.Interfaces;

public interface Toggleable {
	/**
	 * Toggle an NPC's state
	 * 
	 * @return
	 */
	public void toggle();

	/**
	 * Get an NPC's toggle state
	 * 
	 * @return
	 */
	public boolean getToggle();

	/**
	 * Get an NPC's name
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Get an NPC's type
	 * 
	 * @return
	 */
	public String getType();

	/**
	 * Save an NPC's state
	 * 
	 * @return
	 */
	public void saveState();

	/**
	 * Register an NPC
	 * 
	 * @return
	 */
	public void register();
}