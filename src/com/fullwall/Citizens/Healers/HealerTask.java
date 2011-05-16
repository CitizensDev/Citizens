package com.fullwall.Citizens.Healers;

import java.util.Map.Entry;

import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerTask implements Runnable {

	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getNPCList()
				.entrySet()) {
			regenerateHealth(entry.getValue());
		}
	}

	/**
	 * Regenerate a healer's health
	 * 
	 * @param npc
	 */
	private void regenerateHealth(HumanNPC npc) {
		if (npc.isHealer()) {
			if (npc.getHealer().getStrength() < npc.getHealer()
					.getMaxStrength()) {
				npc.getHealer().setStrength(npc.getHealer().getStrength() + 1);
			}
		}
	}
}
