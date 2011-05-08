package com.fullwall.Citizens.CommandExecutors;

import com.fullwall.Citizens.Citizens;

public class CommandHandler {
	private static Citizens plugin;
	private BasicExecutor basic;
	private HealerExecutor healer;
	private TraderExecutor trader;
	private WizardExecutor wizard;
	//private QuesterExecutor quester;
	private TogglerExecutor toggle;

	public CommandHandler(Citizens instance) {
		plugin = instance;
	}

	/**
	 * Register our commands
	 * 
	 * @return
	 */
	public void registerCommands() {
		basic = new BasicExecutor(plugin);
		healer = new HealerExecutor(plugin);
		trader = new TraderExecutor(plugin);
		wizard = new WizardExecutor(plugin);
		//quester = new QuesterExecutor(plugin);
		toggle = new TogglerExecutor(plugin);
		plugin.getCommand("npc").setExecutor(basic);
		plugin.getCommand("citizens").setExecutor(basic);
		plugin.getCommand("basic").setExecutor(basic);
		plugin.getCommand("healer").setExecutor(healer);
		plugin.getCommand("trader").setExecutor(trader);
		plugin.getCommand("wizard").setExecutor(wizard);
		//plugin.getCommand("quester").setExecutor(quester);
		plugin.getCommand("toggle").setExecutor(toggle);
	}
}
