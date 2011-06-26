package com.temp.NPCTypes.Wizards;

import java.util.Map.Entry;

import com.temp.Constants;
import com.temp.NPCTypes.Wizards.WizardManager;
import com.temp.NPCs.NPCManager;
import com.temp.resources.redecouverte.NPClib.HumanNPC;

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