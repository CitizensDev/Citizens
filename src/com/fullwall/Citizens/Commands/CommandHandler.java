package com.fullwall.Citizens.Commands;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Commands.Commands.BasicCommands;
import com.fullwall.Citizens.Commands.Commands.BlacksmithCommands;
import com.fullwall.Citizens.Commands.Commands.GuardCommands;
import com.fullwall.Citizens.Commands.Commands.HealerCommands;
import com.fullwall.Citizens.Commands.Commands.QuestCommands;
import com.fullwall.Citizens.Commands.Commands.QuesterCommands;
import com.fullwall.Citizens.Commands.Commands.ToggleCommands;
import com.fullwall.Citizens.Commands.Commands.TraderCommands;
import com.fullwall.Citizens.Commands.Commands.WizardCommands;

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