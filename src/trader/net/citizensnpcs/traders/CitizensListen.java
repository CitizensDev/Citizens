package net.citizensnpcs.traders;

import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.CitizensListener;

public class CitizensListen extends CitizensListener {
	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		Trader.loadGlobal();
	}
}
