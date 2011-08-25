package net.citizensnpcs.questers.listeners;

import net.citizensnpcs.api.events.CitizensEnableEvent;
import net.citizensnpcs.api.events.CitizensListener;
import net.citizensnpcs.api.events.CitizensReloadEvent;
import net.citizensnpcs.questers.QuestProperties;

public class QuesterCitizensListen extends CitizensListener {

	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		QuestProperties.initialize();
	}

	@Override
	public void onCitizensReload(CitizensReloadEvent event) {
		QuestProperties.load();
	}
}