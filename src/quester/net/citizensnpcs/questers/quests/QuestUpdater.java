package net.citizensnpcs.questers.quests;

import net.citizensnpcs.questers.QuestCancelException;

import org.bukkit.event.Event;

public interface QuestUpdater {
	public Event.Type[] getEventTypes();

	public boolean update(Event event, ObjectiveProgress progress);

	public boolean isCompleted(ObjectiveProgress progress);

	public String getStatus(ObjectiveProgress progress)
			throws QuestCancelException;
}