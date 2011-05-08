package com.fullwall.Citizens.Listeners;

import org.bukkit.event.Event;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;

import com.iConomy.iConomy;

public class PluginListen extends ServerListener {
	private Citizens plugin;
	private PluginManager pm;

	public PluginListen(Citizens plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Register server events
	 */
	public void registerEvents() {
		pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLUGIN_ENABLE, this, Event.Priority.Monitor, plugin);
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
