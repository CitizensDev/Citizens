package net.citizensnpcs.wizards.listeners;

import org.bukkit.Bukkit;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.events.NPCListener;
import net.citizensnpcs.api.events.NPCToggleTypeEvent;
import net.citizensnpcs.wizards.Wizard;
import net.citizensnpcs.wizards.WizardTask;

public class WizardNPCListen extends NPCListener {

	public void onNPCToggleType(NPCToggleTypeEvent event) {
		if (!event.getToggledType().equals("wizard")) {
			return;
		}
		WizardTask task = new WizardTask((Wizard) event.getNPC().getType(
				"wizard"));
		if (event.isToggledOn()) {
			task.addID(Bukkit
					.getServer()
					.getScheduler()
					.scheduleSyncRepeatingTask(Citizens.plugin, task,
							SettingsManager.getInt("WizardManaRegenRate"),
							SettingsManager.getInt("WizardManaRegenRate")));
		} else {
			if (task.isActiveTask()) {
				task.cancel();
			}
		}
	}
}