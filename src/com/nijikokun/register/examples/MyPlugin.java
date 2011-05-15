package com.nijikokun.register.examples;

import com.nijikokun.register.examples.listeners.server;
import com.nijikokun.register.payment.Method;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {
    public PluginDescriptionFile info = null;
    public PluginManager pluginManager = null;

    // This is public so we can
    public Method Method = null;

    public void onDisable() { }

    public void onEnable() {
        pluginManager.registerEvent(Event.Type.PLUGIN_ENABLE, new server(this), Priority.Monitor, this);
        pluginManager.registerEvent(Event.Type.PLUGIN_DISABLE, new server(this), Priority.Monitor, this);
    }
}
