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
				HumanNPC npc = entry.getValue();
				WizardManager.increaseMana(npc, 1);
			}
		}
	}
}