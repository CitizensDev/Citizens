package net.citizensnpcs.questers.listeners;

import net.citizensnpcs.api.event.CitizensDisableEvent;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.CitizensReloadEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCTalkEvent;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.data.PlayerProfile;
import net.citizensnpcs.questers.data.QuestProperties;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuesterCitizensListen implements Listener {
    @EventHandler
    public void onCitizensDisable(CitizensDisableEvent event) {
        PlayerProfile.saveAll();
        // QuestProperties.save(); TODO: commented out until quest saving is
        // finished.
    }

    @EventHandler
    public void onCitizensEnable(CitizensEnableEvent event) {
        Messaging.log("Loaded " + QuestManager.quests().size() + " quests.");
    }

    @EventHandler
    public void onCitizensReload(CitizensReloadEvent event) {
        QuestProperties.load();
    }

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent event) {
        QuestManager.incrementQuest(event.getPlayer(), event);
    }

    @EventHandler
    public void onNPCTalk(NPCTalkEvent event) {
        QuestManager.incrementQuest(event.getPlayer(), event);
    }
}