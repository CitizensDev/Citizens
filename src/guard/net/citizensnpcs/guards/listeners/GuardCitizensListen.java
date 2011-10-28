package net.citizensnpcs.guards.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.CitizensListener;
import net.citizensnpcs.guards.GuardTask;

import org.bukkit.Bukkit;

public class GuardCitizensListen extends CitizensListener {

	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		Bukkit.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(Citizens.plugin, new GuardTask(), 0,
						1);
	}
}