package net.citizensnpcs.healers.listeners;

import org.bukkit.Bukkit;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.event.npc.NPCListener;
import net.citizensnpcs.api.event.npc.NPCToggleTypeEvent;
import net.citizensnpcs.healers.Healer;
import net.citizensnpcs.healers.HealerTask;

public class HealerNPCListen extends NPCListener {

	public void onNPCToggleType(NPCToggleTypeEvent event) {
		if (!event.getToggledType().equals("healer")) {
			return;
		}
		Healer healer = event.getNPC().getType("healer");
		HealerTask task = new HealerTask(healer);
		if (event.isToggledOn()) {
			int delay = healer.getHealthRegenRate();
			task.addID(Bukkit
					.getServer()
					.getScheduler()
					.scheduleSyncRepeatingTask(Citizens.plugin, task, delay,
							delay));
		} else {
			if (task.isActiveTask()) {
				task.cancel();
			}
		}
	}
}