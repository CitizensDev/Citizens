package com.citizens.commands;

import com.citizens.Citizens;
import com.citizens.commands.commands.BasicCommands;
import com.citizens.commands.commands.BlacksmithCommands;
import com.citizens.commands.commands.GuardCommands;
import com.citizens.commands.commands.HealerCommands;
import com.citizens.commands.commands.ToggleCommands;
import com.citizens.commands.commands.TraderCommands;
import com.citizens.commands.commands.WizardCommands;

public class CommandHandler {
	/**
	 * Register our commands
	 * 
	 * @return
	 */
	public static void registerCommands() {
		Citizens.commands.register(BasicCommands.class);
		Citizens.commands.register(BlacksmithCommands.class);
		Citizens.commands.register(GuardCommands.class);
		Citizens.commands.register(HealerCommands.class);
		// Citizens.commands.register(LandlordCommands.class);
		// Citizens.commands.register(QuestCommands.class);
		// Citizens.commands.register(QuesterCommands.class);
		Citizens.commands.register(ToggleCommands.class);
		Citizens.commands.register(TraderCommands.class);
		Citizens.commands.register(WizardCommands.class);
	}
}