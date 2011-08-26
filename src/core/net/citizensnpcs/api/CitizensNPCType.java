package net.citizensnpcs.api;

public abstract class CitizensNPCType {

	/**
	 * Get an NPC type's name
	 * 
	 * @return NPC type's name, use lowercase
	 */
	public abstract String getName();

	/**
	 * Get an NPC's property handler
	 * 
	 * @return NPC type's properties object
	 */
	public abstract Properties getProperties();

	/**
	 * Get the commands for an NPC type
	 * 
	 * @return NPC type's command object
	 */
	public abstract CommandHandler getCommands();

	public abstract CitizensNPC getInstance();

	/**
	 * Register event listeners for an NPC type. Use
	 * CitizensNPCManager.registerEvent
	 */
	public void registerEvents() {
	}
}
