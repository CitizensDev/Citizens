package com.citizens.commands;

import com.citizens.Citizens;
import com.citizens.commands.commands.BasicCommands;
import com.citizens.commands.commands.ToggleCommands;
import com.citizens.commands.commands.WaypointCommands;

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