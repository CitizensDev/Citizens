package net.citizensnpcs.traders;

import net.citizensnpcs.api.event.citizens.CitizensEnableEvent;
import net.citizensnpcs.api.event.citizens.CitizensListener;

public class CitizensListen extends CitizensListener {
	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		TraderProperties.loadGlobal();
	}
}
