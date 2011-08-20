package net.citizensnpcs.healers.listeners;

import org.bukkit.Bukkit;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.events.CitizensEnableEvent;
import net.citizensnpcs.api.events.CitizensListener;
import net.citizensnpcs.healers.HealerTask;

public class HealerCitizensListen extends CitizensListener {

	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		Bukkit.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(Citizens.plugin, new HealerTask(),
						HealerTask.getHealthRegenRate(),
						HealerTask.getHealthRegenRate());
	}
}