package net.citizensnpcs.questers.quests.progress;

import net.citizensnpcs.questers.QuestCancelException;

import org.bukkit.event.Event;

public interface QuestUpdater {
    public Class<? extends Event>[] getEventTypes();

    public String getStatus(ObjectiveProgress progress) throws QuestCancelException;

    public boolean update(Event event, ObjectiveProgress progress);
}