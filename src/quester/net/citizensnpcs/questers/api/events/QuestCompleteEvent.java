package net.citizensnpcs.questers.api.events;

import net.citizensnpcs.questers.quests.CompletedQuest;
import net.citizensnpcs.questers.quests.Quest;

import org.bukkit.entity.Player;

public class QuestCompleteEvent extends QuestEvent {
	private static final long serialVersionUID = 1L;
	private final CompletedQuest completed;

	public QuestCompleteEvent(Quest quest, CompletedQuest completed,
			Player player) {
		super("QuestCompleteEvent", quest, player);
		this.completed = completed;
	}

	public CompletedQuest getCompleted() {
		return completed;
	}
}
