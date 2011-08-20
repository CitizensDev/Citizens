package net.citizensnpcs.questers.listeners;

import com.citizens.properties.properties.QuestProperties;

import net.citizensnpcs.api.events.CitizensEnableEvent;
import net.citizensnpcs.api.events.CitizensListener;

public class QuesterCitizensListen extends CitizensListener {

	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		QuestProperties.initialize();
	}
}