package net.citizensnpcs.wizards.listeners;

import org.bukkit.Bukkit;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.events.CitizensEnableEvent;
import net.citizensnpcs.api.events.CitizensListener;
import net.citizensnpcs.wizards.WizardTask;

public class WizardCitizensListen extends CitizensListener {

	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		Bukkit.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(Citizens.plugin, new WizardTask(),
						SettingsManager.getInt("WizardManaRegenRate"),
						SettingsManager.getInt("WizardManaRegenRate"));
	}
}