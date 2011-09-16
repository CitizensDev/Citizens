package net.citizensnpcs.questers.quests.progress;

import net.citizensnpcs.questers.QuestCancelException;

import org.bukkit.event.Event;

public interface QuestUpdater {
	public Event.Type[] getEventTypes();

	public boolean update(Event event, ObjectiveProgress progress);

	public String getStatus(ObjectiveProgress progress)
			throws QuestCancelException;
}