package net.citizensnpcs.npctypes;

import java.util.List;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.properties.Setting;

public abstract class CitizensNPCType {

	/**
	 * Get the commands for an NPC type
	 * 
	 * @return NPC type's command handler
	 */
	public abstract CommandHandler getCommands();

	/**
	 * Get an NPC type's name
	 * 
	 * @return NPC type's name, use lowercase
	 */
	public abstract String getName();

	public abstract List<Setting> getSettings();

	/**
	 * Get an instance of a type's CitizensNPC subclass
	 * 
	 * @return instance of a type's CitizensNPC subclass
	 */
	public abstract CitizensNPC newInstance();

	/**
	 * Register event listeners for an NPC type. Use
	 * CitizensManager.registerEvent
	 */
	public void onEnable() {
	}
}