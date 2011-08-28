package net.citizensnpcs.healers.listeners;

import java.util.Map.Entry;

import org.bukkit.Bukkit;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.event.citizens.CitizensDisableEvent;
import net.citizensnpcs.api.event.citizens.CitizensEnableEvent;
import net.citizensnpcs.api.event.citizens.CitizensListener;
import net.citizensnpcs.healers.Healer;
import net.citizensnpcs.healers.HealerTask;
import net.citizensnpcs.resources.npclib.HumanNPC;

public class HealerCitizensListen extends CitizensListener {

	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		if (!SettingsManager.getBoolean("RegenHealerHealth")) {
			return;
		}
		for (Entry<Integer, HumanNPC> entry : CitizensManager.getList()
				.entrySet()) {
			if (entry.getValue().isType("healer")) {
				Healer healer = entry.getValue().getType("healer");
				HealerTask task = new HealerTask(healer);
				int delay = healer.getHealthRegenRate();
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