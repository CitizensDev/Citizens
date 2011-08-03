package net.citizensnpcs.commands;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.commands.commands.BasicCommands;
import net.citizensnpcs.commands.commands.ToggleCommands;
import net.citizensnpcs.commands.commands.WaypointCommands;

public class Commands {
	/**
	 * Register our commands
	 * 
	 * @return
	 */
	public static void registerCommands() {
		Citizens.commands.register(BasicCommands.class);
		Citizens.commands.register(ToggleCommands.class);
		Citizens.commands.register(WaypointCommands.class);
	}
}