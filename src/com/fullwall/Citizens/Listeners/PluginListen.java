package com.fullwall.Citizens.Listeners;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;

import com.iConomy.iConomy;

public class PluginListen extends ServerListener {

	private Citizens plugin;

	public PluginListen(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onPluginEnable(PluginEnableEvent event) {
		if (Citizens.economy == null) {
			Plugin iConomy = (plugin).getServer().getPluginManager()
					.getPlugin("iConomy");

			if (iConomy != null) {
				if (iConomy.isEnabled()) {
					Citizens.economy = ((iConomy) iConomy);
					EconomyHandler.setiConomyEnable(true);
				}
			}
		}
	}
}
