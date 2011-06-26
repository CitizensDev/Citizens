package com.Citizens.Commands;

import com.Citizens.Citizens;
import com.Citizens.Commands.Commands.BasicCommands;
import com.Citizens.Commands.Commands.BlacksmithCommands;
import com.Citizens.Commands.Commands.GuardCommands;
import com.Citizens.Commands.Commands.HealerCommands;
import com.Citizens.Commands.Commands.QuestCommands;
import com.Citizens.Commands.Commands.QuesterCommands;
import com.Citizens.Commands.Commands.ToggleCommands;
import com.Citizens.Commands.Commands.TraderCommands;
import com.Citizens.Commands.Commands.WizardCommands;

public class CommandHandler {
	/**
	 * Register our commands
	 * 
	 * @return
	 */
	public static void registerCommands() {
		Citizens.commands.register(QuestCommands.class);
		Citizens.commands.register(BasicCommands.class);
		Citizens.commands.register(HealerCommands.class);
		Citizens.commands.register(TraderCommands.class);
		Citizens.commands.register(WizardCommands.class);
		Citizens.commands.register(BlacksmithCommands.class);
		Citizens.commands.register(QuesterCommands.class);
		Citizens.commands.register(GuardCommands.class);
		Citizens.commands.register(ToggleCommands.class);
	}
}