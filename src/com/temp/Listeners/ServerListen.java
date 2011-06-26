package com.temp.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.PluginManager;

import com.temp.Citizens;
import com.temp.Economy.EconomyHandler;
import com.temp.Interfaces.Listener;
import com.temp.Utils.Messaging;
import com.temp.resources.nijikokun.register.payment.Methods;

public class ServerListen extends ServerListener implements Listener {
	private final Methods methods;

	public ServerListen() {
		this.methods = new Methods();
	}

	@Override
	public void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLUGIN_ENABLE, this,
				Event.Priority.Monitor, Citizens.plugin);
		pm.registerEvent(Event.Type.PLUGIN_DISABLE, this,
				Event.Priority.Monitor, Citizens.plugin);
	}

	@Override
	public void onPluginEnable(PluginEnableEvent event) {
		if (!this.methods.hasMethod()) {
			if (this.methods.setMethod(event.getPlugin())) {
				Citizens.setMethod(this.methods.getMethod());
				EconomyHandler.setServerEconomyEnabled(true);
				Messaging.log("Payment method found ("
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
				Messaging.log("Payment method disabled.");
			}
			EconomyHandler.setServerEconomyEnabled(false);
		}
	}
}