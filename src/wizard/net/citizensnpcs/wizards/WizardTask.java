package net.citizensnpcs.wizards;

import java.util.Map.Entry;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.npcs.NPCManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

public class WizardTask implements Runnable {

	@Override
	public void run() {
		for (Entry<Integer, HumanNPC> entry : NPCManager.getList().entrySet()) {
			if (!entry.getValue().isType("wizard")) {
				return;
			}
			Wizard wizard = entry.getValue().getType("wizard");
			if (SettingsManager.getBoolean("RegenWizardMana")
					&& !wizard.hasUnlimitedMana()) {
				increaseMana(wizard, 1);
			}
		}
	}

	// Increase the mana of a wizard
	private void increaseMana(Wizard wizard, int mana) {
		if (wizard.getMana() + mana < SettingsManager.getInt("WizardMaxMana")) {
			wizard.setMana(wizard.getMana() + mana);
		}
	}
}