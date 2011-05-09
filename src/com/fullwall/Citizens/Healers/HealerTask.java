package com.fullwall.Citizens.Healers;

import java.util.Map.Entry;

import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.HealerPropertyPool;
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
			int UID = npc.getUID();
			if (HealerPropertyPool.getStrength(UID) < HealerPropertyPool
					.getMaxStrength(UID)) {
				HealerPropertyPool.saveStrength(UID,
						HealerPropertyPool.getStrength(UID) + 1);
			}
		}
	}
}
