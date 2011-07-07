package com.citizens.npctypes.wizards;

import java.util.Map.Entry;

import com.citizens.Constants;
import com.citizens.npcs.NPCManager;
import com.citizens.resources.npclib.HumanNPC;

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