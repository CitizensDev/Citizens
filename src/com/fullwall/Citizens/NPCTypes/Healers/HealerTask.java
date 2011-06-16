package com.fullwall.Citizens.NPCTypes.Healers;

import java.util.Map.Entry;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

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
		if (npc.isHealer()) {
			if (npc.getHealer().getHealth() < npc.getHealer().getMaxHealth()) {
				npc.getHealer().setHealth(npc.getHealer().getHealth() + 1);
			}
		}
	}

	/**
	 * Get the health regeneration rate for a healer based on its level
	 * 
	 * @return
	 */
	public static int getHealthRegenRate() {
		int delay = 0;
		if (!NPCManager.getList().isEmpty()) {
			for (Entry<Integer, HumanNPC> entry : NPCManager.getList()
					.entrySet()) {
				delay = Constants.healerHealthRegenIncrement
						* (11 - (entry.getValue().getHealer().getLevel()));
			}
		} else {
			delay = 12000;
		}
		return delay;
	}
}