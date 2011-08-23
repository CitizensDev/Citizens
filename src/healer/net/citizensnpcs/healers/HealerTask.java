package net.citizensnpcs.healers;

import java.util.Map.Entry;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

public class HealerTask implements Runnable {

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : CitizensManager.getList()
				.entrySet()) {
			if (SettingsManager.getBoolean("RegenHealerHealth")) {
				regenerateHealth(entry.getValue());
			}
		}
	}

	// Regenerate a healer's health
	private void regenerateHealth(HumanNPC npc) {
		if (npc.isType("healer")) {
			Healer healer = npc.getType("healer");
			if (healer.getHealth() < healer.getMaxHealth()) {
				healer.setHealth(healer.getHealth() + 1);
			}
		}
	}

	// Get the health regeneration rate for a healer based on its level
	public static int getHealthRegenRate() {
		int delay = SettingsManager.getInt("HealerHealthRegenIncrement");
		if (!CitizensManager.getList().isEmpty()) {
			for (Entry<Integer, HumanNPC> entry : CitizensManager.getList()
					.entrySet()) {
				if (entry.getValue().isType("healer")) {
					delay = delay
							* (11 - ((Healer) (entry.getValue()
									.getType("healer"))).getLevel());
				}
			}
		}
		return delay;
	}
}