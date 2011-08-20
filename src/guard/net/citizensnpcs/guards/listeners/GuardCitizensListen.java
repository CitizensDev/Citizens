package net.citizensnpcs.guards.listeners;

import org.bukkit.Bukkit;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.events.CitizensEnableEvent;
import net.citizensnpcs.api.events.CitizensListener;
import net.citizensnpcs.guards.GuardTask;

public class GuardCitizensListen extends CitizensListener {

	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		Bukkit.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(Citizens.plugin, new GuardTask(),
						SettingsManager.getInt("TickDelay"),
						SettingsManager.getInt("TickDelay"));
	}
}