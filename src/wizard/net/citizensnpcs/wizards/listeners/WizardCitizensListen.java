package net.citizensnpcs.wizards.listeners;

import java.util.Map.Entry;

import org.bukkit.Bukkit;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.event.citizens.CitizensDisableEvent;
import net.citizensnpcs.api.event.citizens.CitizensEnableEvent;
import net.citizensnpcs.api.event.citizens.CitizensListener;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.wizards.Wizard;
import net.citizensnpcs.wizards.WizardTask;

public class WizardCitizensListen extends CitizensListener {

	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		if (!SettingsManager.getBoolean("RegenWizardMana")) {
			return;
		}
		for (Entry<Integer, HumanNPC> entry : CitizensManager.getList()
				.entrySet()) {
			if (entry.getValue().isType("wizard")) {
				Wizard wizard = entry.getValue().getType("wizard");
				WizardTask task = new WizardTask(wizard);
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