package net.citizensnpcs.questers.api.events;

import net.citizensnpcs.api.event.CitizensEvent;
import net.citizensnpcs.questers.quests.Quest;

import org.bukkit.entity.Player;

public abstract class QuestEvent extends CitizensEvent {
    private final Player player;
    private final Quest quest;

    protected QuestEvent(Quest quest, Player player) {
        this.quest = quest;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Quest getQuest() {
        return quest;
    }
}
