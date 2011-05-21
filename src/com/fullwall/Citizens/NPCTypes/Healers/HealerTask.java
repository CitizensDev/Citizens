package com.fullwall.Citizens.NPCTypes.Healers;

import java.util.Map.Entry;

import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerTask implements Runnable {

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
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
			if (npc.getHealer().getHealth() < npc.getHealer().getMaxHealth()) {
				npc.getHealer().setHealth(npc.getHealer().getHealth() + 1);
			}
		}
	}
}