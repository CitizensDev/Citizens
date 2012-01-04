package net.citizensnpcs.wizards.listeners;

import net.citizensnpcs.api.event.CitizensDisableEvent;
import net.citizensnpcs.api.event.CitizensListener;
import net.citizensnpcs.wizards.WizardTask;

public class WizardCitizensListen extends CitizensListener {
	@Override
	public void onCitizensDisable(CitizensDisableEvent event) {
		WizardTask.cancelTasks();
	}
}