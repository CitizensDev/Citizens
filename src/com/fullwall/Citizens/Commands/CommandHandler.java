package com.fullwall.Citizens.Commands;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Commands.CommandExecutors.BanditExecutor;
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
	private final Citizens plugin;

	public CommandHandler(Citizens plugin) {
		this.plugin = plugin;
	}

	/**
	 * Register our commands
	 * 
	 * @return
	 */
	public void registerCommands() {
		plugin.getCommand("npc").setExecutor(new BasicExecutor(plugin));
		plugin.getCommand("citizens").setExecutor(new BasicExecutor(plugin));
		plugin.getCommand("basic").setExecutor(new BasicExecutor(plugin));
		plugin.getCommand("healer").setExecutor(new HealerExecutor(plugin));
		plugin.getCommand("trader").setExecutor(new TraderExecutor(plugin));
		plugin.getCommand("wizard").setExecutor(new WizardExecutor(plugin));
		plugin.getCommand("blacksmith").setExecutor(
				new BlacksmithExecutor(plugin));
		plugin.getCommand("quester").setExecutor(new QuesterExecutor(plugin));
		plugin.getCommand("bandit").setExecutor(new BanditExecutor(plugin));
		plugin.getCommand("guard").setExecutor(new GuardExecutor(plugin));
		plugin.getCommand("tog").setExecutor(new TogglerExecutor(plugin));
		plugin.getCommand("quests").setExecutor(new QuestsExecutor(plugin));
	}
}