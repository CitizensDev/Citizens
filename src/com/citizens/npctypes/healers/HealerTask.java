package com.citizens.npctypes.healers;

import java.util.Map.Entry;

import com.citizens.SettingsManager.Constant;
import com.citizens.npcs.NPCManager;
import com.citizens.resources.npclib.HumanNPC;

public class HealerTask implements Runnable {
	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			if (Constant.RegenHealerHealth.getBoolean()) {
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
			HealerNPC healer = npc.getToggleable("healer");
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
		int delay = Constant.HealerHealthRegenIncrement.getInt();
		if (!NPCManager.getList().isEmpty()) {
			for (Entry<Integer, HumanNPC> entry : NPCManager.getList()
					.entrySet()) {
				if (entry.getValue().isType("healer")) {
					delay = Constant.HealerHealthRegenIncrement.getInt()
							* (11 - ((HealerNPC) (entry.getValue()
									.getToggleable("healer"))).getLevel());
				}
			}
		}
		return delay;
	}
}