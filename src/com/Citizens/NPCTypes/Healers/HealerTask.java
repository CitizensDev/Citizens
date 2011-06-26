package com.Citizens.NPCTypes.Healers;

import java.util.Map.Entry;

import com.Citizens.Constants;
import com.Citizens.resources.redecouverte.NPClib.HumanNPC;
import com.Citizens.NPCTypes.Healers.HealerNPC;
import com.Citizens.NPCs.NPCManager;

public class HealerTask implements Runnable {
	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			if (Constants.regenHealerHealth) {
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
		int delay = Constants.healerHealthRegenIncrement;
		if (!NPCManager.getList().isEmpty()) {
			for (Entry<Integer, HumanNPC> entry : NPCManager.getList()
					.entrySet()) {
				if (entry.getValue().isType("healer")) {
					delay = Constants.healerHealthRegenIncrement
							* (11 - ((HealerNPC) (entry.getValue()
									.getToggleable("healer"))).getLevel());
				}
			}
		}
		return delay;
	}
}