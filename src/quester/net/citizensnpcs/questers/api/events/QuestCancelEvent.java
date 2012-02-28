package net.citizensnpcs.questers.api.events;

import net.citizensnpcs.questers.quests.Quest;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class QuestCancelEvent extends QuestEvent {
    public QuestCancelEvent(Quest quest, Player player) {
        super(quest, player);
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
