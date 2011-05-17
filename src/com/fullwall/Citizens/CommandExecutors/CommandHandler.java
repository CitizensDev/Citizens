package com.fullwall.Citizens.CommandExecutors;

import com.fullwall.Citizens.Citizens;

public class CommandHandler {
	private Citizens plugin;
	private BasicExecutor basic;
	private HealerExecutor healer;
	private TraderExecutor trader;
	private WizardExecutor wizard;
	private BlacksmithExecutor blacksmith;
	private QuesterExecutor quester;
	private BanditExecutor bandit;
	private TogglerExecutor toggle;

	public CommandHandler(Citizens plugin) {
		this.plugin = plugin;
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
		blacksmith = new BlacksmithExecutor(plugin);
		quester = new QuesterExecutor(plugin);
		bandit = new BanditExecutor(plugin);
		toggle = new TogglerExecutor(plugin);
		plugin.getCommand("npc").setExecutor(basic);
		plugin.getCommand("citizens").setExecutor(basic);
		plugin.getCommand("basic").setExecutor(basic);
		plugin.getCommand("healer").setExecutor(healer);
		plugin.getCommand("trader").setExecutor(trader);
		plugin.getCommand("wizard").setExecutor(wizard);
		plugin.getCommand("blacksmith").setExecutor(blacksmith);
		plugin.getCommand("quester").setExecutor(quester);
		plugin.getCommand("bandit").setExecutor(bandit);
		plugin.getCommand("toggle").setExecutor(toggle);
	}
}