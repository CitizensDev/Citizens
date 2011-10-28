package net.citizensnpcs.questers.listeners;

import net.citizensnpcs.api.event.CitizensDisableEvent;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.CitizensListener;
import net.citizensnpcs.api.event.CitizensReloadEvent;
import net.citizensnpcs.api.event.NPCPlayerEvent;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.data.PlayerProfile;
import net.citizensnpcs.questers.data.QuestProperties;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.event.Event;

public class QuesterCitizensListen extends CitizensListener {
	@Override
	public void onCitizensEnable(CitizensEnableEvent event) {
		Messaging.log("Loaded " + QuestManager.quests().size() + " quests.");
	}

	@Override
	public void onCitizensReload(CitizensReloadEvent event) {
		QuestProperties.load();
	}

	@Override
	public void onCitizensDisable(CitizensDisableEvent event) {
		PlayerProfile.saveAll();
		// QuestProperties.save(); TODO: commented out until quest saving is
		// finished.
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