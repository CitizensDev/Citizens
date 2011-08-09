package net.citizensnpcs.alchemists;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.commands.CommandHandler;

public class AlchemistCommands implements CommandHandler {

	@Override
	public void addPermissions() {
		PermissionManager.addPerm("alchemist.use.help");
		PermissionManager.addPerm("alchemist.use.interact");
	}
}