package net.citizensnpcs.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Plugins;
import net.citizensnpcs.economy.Economy;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.PluginManager;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ServerListen extends ServerListener implements Listener {
	@Override
	public void registerEvents(Citizens plugin) {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Type.PLUGIN_ENABLE, this, Priority.Monitor, plugin);
		pm.registerEvent(Type.PLUGIN_DISABLE, this, Priority.Monitor, plugin);
	}

	@Override
	public void onPluginEnable(PluginEnableEvent event) {
		Economy.tryEnableEconomy();
		if (event.getPlugin().getDescription().getName()
				.equalsIgnoreCase("WorldGuard")) {
			Plugins.worldGuard = (WorldGuardPlugin) event.getPlugin();
		}
	}

	@Override
	public void onPluginDisable(PluginDisableEvent event) {
		Economy.tryDisableEconomy(event.getPlugin());
	}
}