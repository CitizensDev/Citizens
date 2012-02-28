package net.citizensnpcs.questers.api.events;

import net.citizensnpcs.questers.quests.CompletedQuest;
import net.citizensnpcs.questers.quests.Quest;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class QuestCompleteEvent extends QuestEvent {
    private final CompletedQuest completed;

    public QuestCompleteEvent(Quest quest, CompletedQuest completed, Player player) {
        super(quest, player);
        this.completed = completed;
    }

    public CompletedQuest getCompleted() {
        return completed;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
