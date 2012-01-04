package net.citizensnpcs.healers.listeners;

import net.citizensnpcs.api.event.CitizensDisableEvent;
import net.citizensnpcs.api.event.CitizensListener;
import net.citizensnpcs.healers.HealerTask;

public class HealerCitizensListen extends CitizensListener {

	@Override
	public void onCitizensDisable(CitizensDisableEvent event) {
		HealerTask.cancelTasks();
	}

}