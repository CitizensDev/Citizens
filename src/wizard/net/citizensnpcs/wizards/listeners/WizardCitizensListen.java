package net.citizensnpcs.wizards.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;
import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.event.CitizensDisableEvent;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.CitizensListener;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.wizards.WizardTask;

import org.bukkit.Bukkit;

public class WizardCitizensListen extends CitizensListener {

	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		if (!Settings.getBoolean("RegenWizardMana")) {
			return;
		}
		for (HumanNPC entry : CitizensManager.getList().values()) {
			if (entry.isType("wizard")) {
				WizardTask task = new WizardTask(entry);
				task.addID(Bukkit
						.getServer()
						.getScheduler()
						.scheduleSyncRepeatingTask(Citizens.plugin, task,
								Settings.getInt("WizardManaRegenRate"),
								Settings.getInt("WizardManaRegenRate")));
			}
		}
	}

	@Override
	public void onCitizensDisable(CitizensDisableEvent event) {
		WizardTask.cancelTasks();
	}
}