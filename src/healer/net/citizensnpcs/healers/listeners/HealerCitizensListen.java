package net.citizensnpcs.healers.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;
import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.event.citizens.CitizensDisableEvent;
import net.citizensnpcs.api.event.citizens.CitizensEnableEvent;
import net.citizensnpcs.api.event.citizens.CitizensListener;
import net.citizensnpcs.healers.Healer;
import net.citizensnpcs.healers.HealerTask;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Bukkit;

public class HealerCitizensListen extends CitizensListener {

	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		if (!Settings.getBoolean("RegenHealerHealth")) {
			return;
		}
		for (HumanNPC entry : CitizensManager.getList().values()) {
			if (entry.isType("healer")) {
				HealerTask task = new HealerTask(entry);
				int delay = ((Healer) entry.getType("healer"))
						.getHealthRegenRate();
				task.addID(Bukkit
						.getServer()
						.getScheduler()
						.scheduleSyncRepeatingTask(Citizens.plugin, task,
								delay, delay));
			}
		}
	}

	@Override
	public void onCitizensDisable(CitizensDisableEvent event) {
		HealerTask.cancelTasks();
	}
}