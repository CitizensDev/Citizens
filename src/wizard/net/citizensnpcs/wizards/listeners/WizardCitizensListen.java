package net.citizensnpcs.wizards.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.event.citizens.CitizensDisableEvent;
import net.citizensnpcs.api.event.citizens.CitizensEnableEvent;
import net.citizensnpcs.api.event.citizens.CitizensListener;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.wizards.WizardTask;

import org.bukkit.Bukkit;

public class WizardCitizensListen extends CitizensListener {

	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		if (!SettingsManager.getBoolean("RegenWizardMana")) {
			return;
		}
		for (HumanNPC entry : CitizensManager.getList().values()) {
			if (entry.isType("wizard")) {
				WizardTask task = new WizardTask(entry);
				task.addID(Bukkit
						.getServer()
						.getScheduler()
						.scheduleSyncRepeatingTask(Citizens.plugin, task,
								SettingsManager.getInt("WizardManaRegenRate"),
								SettingsManager.getInt("WizardManaRegenRate")));
			}
		}
	}

	@Override
	public void onCitizensDisable(CitizensDisableEvent event) {
		WizardTask.cancelTasks();
	}
}