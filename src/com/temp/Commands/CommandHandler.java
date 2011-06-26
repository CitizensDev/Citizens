package com.temp.Commands;

import com.temp.Citizens;
import com.temp.Commands.Commands.BasicCommands;
import com.temp.Commands.Commands.BlacksmithCommands;
import com.temp.Commands.Commands.GuardCommands;
import com.temp.Commands.Commands.HealerCommands;
import com.temp.Commands.Commands.QuestCommands;
import com.temp.Commands.Commands.QuesterCommands;
import com.temp.Commands.Commands.ToggleCommands;
import com.temp.Commands.Commands.TraderCommands;
import com.temp.Commands.Commands.WizardCommands;

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