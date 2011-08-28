package net.citizensnpcs.api;

import org.bukkit.command.CommandSender;

/**
 * Every main NPC type class requires a class that implements this.
 */
public abstract class CommandHandler {

	/**
	 * Add permissions to be registered by superperms, register permissions like
	 * so: CitizensManager.addPermission(perm) Note: You must register
	 * non-command permissions in this method...ex) 'wizard.use.interact' <--
	 * handles right/left clicking wizard
	 */
	public abstract void addPermissions();

	/**
	 * Send the help page for an NPC type, recommended to use with command
	 * '/(typename) help (page)'
	 * 
	 * @param sender
	 *            Sender to send the page to
	 * @param page
	 *            page number
	 */
	public void sendHelpPage(CommandSender sender, int page) {
	}
}