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
		// TODO remove commented code when modularization is complete
		Citizens.commands.register(BasicCommands.class);
		// Citizens.commands.register(BlacksmithCommands.class);
		// Citizens.commands.register(GuardCommands.class);
		// Citizens.commands.register(HealerCommands.class);
		// Citizens.commands.register(QuestCommands.class);
		// Citizens.commands.register(QuesterCommands.class);
		Citizens.commands.register(ToggleCommands.class);
		// Citizens.commands.register(TraderCommands.class);
		Citizens.commands.register(WaypointCommands.class);
		// Citizens.commands.register(WizardCommands.class);
	}
}