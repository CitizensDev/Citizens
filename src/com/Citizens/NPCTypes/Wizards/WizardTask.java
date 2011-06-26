package com.Citizens.NPCTypes.Wizards;

import java.util.Map.Entry;

import com.Citizens.Constants;
import com.Citizens.resources.redecouverte.NPClib.HumanNPC;
import com.Citizens.NPCTypes.Wizards.WizardManager;
import com.Citizens.NPCs.NPCManager;

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