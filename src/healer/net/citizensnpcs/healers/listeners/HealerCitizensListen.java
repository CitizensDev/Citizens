package net.citizensnpcs.healers.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;
import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.event.CitizensDisableEvent;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.healers.Healer;
import net.citizensnpcs.healers.HealerTask;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HealerCitizensListen implements Listener {

    @EventHandler
    public void onCitizensEnable(CitizensEnableEvent event) {
        if (!Settings.getBoolean("RegenHealerHealth")) {
            return;
        }
        for (HumanNPC entry : CitizensManager.getList().values()) {
            if (entry.isType("healer")) {
                HealerTask task = new HealerTask(entry);
                int delay = ((Healer) entry.getType("healer")).getHealthRegenRate();
                task.addID(Bukkit.getServer().getScheduler()
                        .scheduleSyncRepeatingTask(Citizens.plugin, task, delay, delay));
            }
        }
    }

    @EventHandler
    public void onCitizensDisable(CitizensDisableEvent event) {
        HealerTask.cancelTasks();
    }
}