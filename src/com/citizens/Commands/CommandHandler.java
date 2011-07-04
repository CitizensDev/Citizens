package com.citizens.Commands;

import com.citizens.Citizens;
import com.citizens.Commands.Commands.BasicCommands;
import com.citizens.Commands.Commands.BlacksmithCommands;
import com.citizens.Commands.Commands.HealerCommands;
import com.citizens.Commands.Commands.ToggleCommands;
import com.citizens.Commands.Commands.TraderCommands;
import com.citizens.Commands.Commands.WizardCommands;

public class CommandHandler {
	/**
	 * Register our commands
	 * 
	 * @return
	 */
	public static void registerCommands() {
		Citizens.commands.register(BasicCommands.class);
		Citizens.commands.register(BlacksmithCommands.class);
		// Citizens.commands.register(GuardCommands.class);
		Citizens.commands.register(HealerCommands.class);
		// Citizens.commands.register(LandlordCommands.class);
		// Citizens.commands.register(QuestCommands.class);
		// Citizens.commands.register(QuesterCommands.class);
		Citizens.commands.register(ToggleCommands.class);
		Citizens.commands.register(TraderCommands.class);
		Citizens.commands.register(WizardCommands.class);
	}
}