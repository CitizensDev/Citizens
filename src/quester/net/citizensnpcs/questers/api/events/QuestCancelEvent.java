package net.citizensnpcs.questers.api.events;

import net.citizensnpcs.questers.quests.Quest;

import org.bukkit.entity.Player;

public class QuestCancelEvent extends QuestEvent {
	private static final long serialVersionUID = 1L;

	public QuestCancelEvent(Quest quest, Player player) {
		super("QuestCancelEvent", quest, player);
	}
}
