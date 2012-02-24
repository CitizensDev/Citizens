package net.citizensnpcs.listeners;

import net.citizensnpcs.Economy;
import net.citizensnpcs.Plugins;
import net.citizensnpcs.resources.register.payment.Methods;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ServerListen implements Listener {
    private final Methods methods = new Methods();

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (!Methods.hasMethod()) {
            if (Methods.setMethod(Bukkit.getPluginManager())) {
                Economy.setMethod(Methods.getMethod());
                Economy.setServerEconomyEnabled(true);
                Messaging.log("Economy plugin found (" + Methods.getMethod().getName() + " v"
                        + Methods.getMethod().getVersion() + ")");
            }
        }
        if (event.getPlugin().getDescription().getName().equalsIgnoreCase("WorldGuard")) {
            Plugins.worldGuard = (WorldGuardPlugin) event.getPlugin();
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (this.methods != null && Methods.hasMethod() && Methods.getMethod().getPlugin().equals(event.getPlugin())) {
            Economy.setServerEconomyEnabled(false);
        }
    }
}