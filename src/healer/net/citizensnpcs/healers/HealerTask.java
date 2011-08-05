package net.citizensnpcs.healers;

import java.util.Map.Entry;

import net.citizensnpcs.npcs.NPCManager;
import net.citizensnpcs.properties.SettingsManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

public class HealerTask implements Runnable {

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			if (SettingsManager.getBoolean("RegenHealerHealth")) {
				regenerateHealth(entry.getValue());
			}
		}
	}

	/**
	 * Regenerate a healer's health
	 * 
	 * @param npc
	 */
	private void regenerateHealth(HumanNPC npc) {
		if (npc.isType("healer")) {
			Healer healer = npc.getType("healer");
			if (healer.getHealth() < healer.getMaxHealth()) {
				healer.setHealth(healer.getHealth() + 1);
			}
		}
	}

	/**
	 * Get the health regeneration rate for a healer based on its level
	 * 
	 * @return
	 */
	public static int getHealthRegenRate() {
		int delay = SettingsManager.getInt("HealerHealthRegenIncrement");
		if (!NPCManager.getList().isEmpty()) {
			for (Entry<Integer, HumanNPC> entry : NPCManager.getList()
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