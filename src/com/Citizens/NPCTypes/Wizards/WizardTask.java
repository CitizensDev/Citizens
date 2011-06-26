package com.citizens.NPCTypes.Wizards;

import java.util.Map.Entry;

import com.citizens.Constants;
import com.citizens.NPCTypes.Wizards.WizardManager;
import com.citizens.NPCs.NPCManager;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;

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