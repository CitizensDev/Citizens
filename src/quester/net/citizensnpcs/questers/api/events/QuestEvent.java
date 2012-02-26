package net.citizensnpcs.questers.api.events;

import net.citizensnpcs.api.event.CitizensEvent;
import net.citizensnpcs.questers.quests.Quest;

import org.bukkit.entity.Player;

public class QuestEvent extends CitizensEvent {
    private static final long serialVersionUID = 1L;
    private final Quest quest;
    private final Player player;

    protected QuestEvent(Quest quest, Player player) {
        this.quest = quest;
        this.player = player;
    }

    public Quest getQuest() {
        return quest;
    }

    public Player getPlayer() {
        return player;
    }
}
