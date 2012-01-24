package net.citizensnpcs.wizards.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;
import net.citizensnpcs.api.event.NPCToggleTypeEvent;
import net.citizensnpcs.wizards.WizardTask;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WizardNPCListen implements Listener {
    @EventHandler
    public void onNPCToggleType(NPCToggleTypeEvent event) {
        if (!event.getToggledType().equals("wizard")) {
            return;
        }
        WizardTask task = new WizardTask(event.getNPC());
        if (event.isToggledOn()) {
            task.addID(Bukkit
                    .getServer()
                    .getScheduler()
                    .scheduleSyncRepeatingTask(Citizens.plugin, task, Settings.getInt("WizardManaRegenRate"),
                            Settings.getInt("WizardManaRegenRate")));
        } else {
            if ((task = WizardTask.getTask(event.getNPC().getUID())) != null) {
                task.cancel();
            }
        }
    }
}