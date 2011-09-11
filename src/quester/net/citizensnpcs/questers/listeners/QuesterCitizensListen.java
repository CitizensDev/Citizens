package net.citizensnpcs.questers.listeners;

import net.citizensnpcs.api.event.citizens.CitizensDisableEvent;
import net.citizensnpcs.api.event.citizens.CitizensEnableEvent;
import net.citizensnpcs.api.event.citizens.CitizensListener;
import net.citizensnpcs.api.event.citizens.CitizensReloadEvent;
import net.citizensnpcs.api.event.npc.NPCPlayerEvent;
import net.citizensnpcs.questers.PlayerProfile;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.QuestProperties;

import org.bukkit.event.Event;

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

	@Override
	public void onCustomEvent(Event event) {
		super.onCustomEvent(event);
		if (event instanceof NPCPlayerEvent) {
			QuestManager.incrementQuest(((NPCPlayerEvent) event).getPlayer(),
					event);
		}
	}
}