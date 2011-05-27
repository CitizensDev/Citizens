package com.fullwall.Citizens.Listeners;

import org.bukkit.event.Event;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Interfaces.Listener;

import com.nijikokun.register.payment.Methods;

public class PluginListen extends ServerListener implements Listener {
	private Citizens plugin;
	private PluginManager pm;
	private Methods methods;

	public PluginListen(Citizens plugin) {
		this.plugin = plugin;
		this.methods = new Methods("iConomy");
	}

	@Override
	public void registerEvents() {
		pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLUGIN_ENABLE, this,
				Event.Priority.Monitor, plugin);
		pm.registerEvent(Event.Type.PLUGIN_DISABLE, this,
				Event.Priority.Monitor, plugin);
	}

	@Override
	public void onPluginEnable(PluginEnableEvent event) {
		if (!this.methods.hasMethod()) {
			if (this.methods.setMethod(event.getPlugin())) {
				Citizens.setMethod(this.methods.getMethod());
				EconomyHandler.setServerEconomyEnabled(true);
				System.out.println("[Citizens]: Payment method found ("
						+ methods.getMethod().getName() + " version: "
						+ methods.getMethod().getVersion() + ")");
			}
		}
	}

	@Override
	public void onPluginDisable(PluginDisableEvent event) {
		if (this.methods != null && this.methods.hasMethod()) {
			Boolean check = this.methods.checkDisabled(event.getPlugin());
			if (check) {
				Citizens.log.info("[Citizens]: Payment method disabled.");
			}
			EconomyHandler.setServerEconomyEnabled(false);
		}
	}
}