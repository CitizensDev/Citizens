package net.citizensnpcs.wizards.listeners;

import org.bukkit.Bukkit;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.event.npc.NPCListener;
import net.citizensnpcs.api.event.npc.NPCToggleTypeEvent;
import net.citizensnpcs.wizards.WizardTask;

public class WizardNPCListen extends NPCListener {

	public void onNPCToggleType(NPCToggleTypeEvent event) {
		if (!event.getToggledType().equals("wizard")) {
			return;
		}
		WizardTask task = new WizardTask(event.getNPC());
		if (event.isToggledOn()) {
			task.addID(Bukkit
					.getServer()
					.getScheduler()
					.scheduleSyncRepeatingTask(Citizens.plugin, task,
							SettingsManager.getInt("WizardManaRegenRate"),
							SettingsManager.getInt("WizardManaRegenRate")));
		} else {
			if ((task = WizardTask.getTask(event.getNPC().getUID())) != null) {
				task.cancel();
			}
		}
	}
}