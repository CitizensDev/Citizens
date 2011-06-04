package com.fullwall.Citizens.NPCTypes.Wizards;

import java.util.Map.Entry;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardTask implements Runnable {

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			if (Constants.regenWizardMana) {
				regenerateMana(entry.getValue());
			}
		}
	}

	/**
	 * Regenerate's a wizard's mana
	 * 
	 * @param npc
	 */
	private void regenerateMana(HumanNPC npc) {
		if (npc.getWizard().getMana() < Constants.maxWizardMana) {
			WizardManager.increaseMana(npc, 1);
		}
	}
}