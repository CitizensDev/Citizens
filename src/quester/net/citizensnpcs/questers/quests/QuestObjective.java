package net.citizensnpcs.questers.quests;

import org.bukkit.event.Event;

public interface QuestObjective {
	public Event.Type[] getEventTypes();

	public boolean update(Event event, ObjectiveProgress progress);

	public boolean isCompleted(ObjectiveProgress progress);

	public String getStatus(ObjectiveProgress progress);
}