package net.citizensnpcs.questers.listeners;

import net.citizensnpcs.api.event.citizens.CitizensDisableEvent;
import net.citizensnpcs.api.event.citizens.CitizensEnableEvent;
import net.citizensnpcs.api.event.citizens.CitizensListener;
import net.citizensnpcs.api.event.citizens.CitizensReloadEvent;
import net.citizensnpcs.questers.PlayerProfile;
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

	@Override
	public void onCitizensDisable(CitizensDisableEvent event) {
		PlayerProfile.saveAll();
	}
}