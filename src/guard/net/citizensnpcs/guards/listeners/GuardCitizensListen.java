package net.citizensnpcs.guards.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.guards.GuardTask;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GuardCitizensListen implements Listener {
    @EventHandler
    public void onCitizensEnable(CitizensEnableEvent event) {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Citizens.plugin, new GuardTask(), 0, 1);
    }
}