package com.citizens.npctypes.wizards;

import java.util.Map.Entry;

import com.citizens.npcs.NPCManager;
import com.citizens.properties.SettingsManager;
import com.citizens.resources.npclib.HumanNPC;

public class WizardTask implements Runnable {

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			HumanNPC npc = entry.getValue();
			if (!npc.isType("wizard")) {
				return;
			}
			Wizard wizard = npc.getType("wizard");
			if (SettingsManager.getBoolean("general.wizards.regen-mana")
					&& !wizard.hasUnlimitedMana()) {
				WizardManager.increaseMana(npc, 1);
			}
		}
	}
}