package net.citizensnpcs.commands;

import org.bukkit.command.CommandSender;

public abstract class CommandHandler {

	/**
	 * Add permissions to be registered by superperms, register permissions like
	 * so: PermissionManager.addPermission(perm) Note: perm cannot contain
	 * "citizens.". Also, you must register non-command permissions in this
	 * method...ex) 'wizard.use.interact'
	 * 
	 * @return
	 */
	public abstract void addPermissions();

	/**
	 * Sends the help page for an NPC type; Note: Use built-in HelpUtils class
	 * 
	 * @param sender
	 *            sender to send the help page(s) to
	 */
	public void sendHelp(CommandSender sender, int page) {
	}
}