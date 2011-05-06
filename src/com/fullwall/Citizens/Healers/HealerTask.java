package com.fullwall.Citizens.Healers;

import java.util.Map.Entry;

import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.HealerPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerTask implements Runnable {

	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getNPCList()
				.entrySet()) {
			HumanNPC npc = entry.getValue();
			regenerateHealth(npc, HealerPropertyPool.getLevel(npc.getUID()));
		}
	}

	/**
	 * Regenerate a healer's health based on what level they are
	 * 
	 * @param npc
	 * @param level
	 */
	private void regenerateHealth(HumanNPC npc, int level) {
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
