package net.citizensnpcs.commands;

import org.bukkit.command.CommandSender;

/**
 * Every main NPC type class requires a class that implements this.
 */
public abstract class CommandHandler {
	/**
	 * Add permissions to be registered by superperms
	 */
	public abstract void addPermissions();

	/**
	 * Send the help page for an NPC type
	 * 
	 * @param sender
	 *            Sender to send the page to
	 */
	public void sendHelpPage(CommandSender sender) {
	}
}