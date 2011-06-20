package com.fullwall.Citizens.Commands;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Commands.CommandExecutors.BasicExecutor;
import com.fullwall.Citizens.Commands.CommandExecutors.BlacksmithExecutor;
import com.fullwall.Citizens.Commands.CommandExecutors.GuardExecutor;
import com.fullwall.Citizens.Commands.CommandExecutors.HealerExecutor;
import com.fullwall.Citizens.Commands.CommandExecutors.QuesterExecutor;
import com.fullwall.Citizens.Commands.CommandExecutors.QuestsExecutor;
import com.fullwall.Citizens.Commands.CommandExecutors.TogglerExecutor;
import com.fullwall.Citizens.Commands.CommandExecutors.TraderExecutor;
import com.fullwall.Citizens.Commands.CommandExecutors.WizardExecutor;

public class CommandHandler {
	/**
	 * Register our commands
	 * 
	 * @return
	 */
	public static void registerCommands() {
		// Example register of new command system.
		// Citizens.commands.register(BasicExecutor.class);
		// Note this also handles all command names that are registered in the
		// class (npc, citizens, and basic don't all need to be registered
		// separately).
		Citizens.plugin.getCommand("npc").setExecutor(new BasicExecutor());
		Citizens.plugin.getCommand("citizens").setExecutor(new BasicExecutor());
		Citizens.plugin.getCommand("basic").setExecutor(new BasicExecutor());
		Citizens.plugin.getCommand("healer").setExecutor(new HealerExecutor());
		Citizens.plugin.getCommand("trader").setExecutor(new TraderExecutor());
		Citizens.plugin.getCommand("wizard").setExecutor(new WizardExecutor());
		Citizens.plugin.getCommand("blacksmith").setExecutor(
				new BlacksmithExecutor());
		Citizens.plugin.getCommand("quester")
				.setExecutor(new QuesterExecutor());
		Citizens.plugin.getCommand("guard").setExecutor(new GuardExecutor());
		Citizens.plugin.getCommand("tog").setExecutor(new TogglerExecutor());
		Citizens.plugin.getCommand("quests").setExecutor(new QuestsExecutor());
	}
}