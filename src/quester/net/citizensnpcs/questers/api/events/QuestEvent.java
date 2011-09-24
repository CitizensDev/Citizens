package net.citizensnpcs.questers.api.events;

import net.citizensnpcs.api.event.citizens.CitizensEvent;
import net.citizensnpcs.questers.quests.Quest;

import org.bukkit.entity.Player;

public class QuestEvent extends CitizensEvent {
	private static final long serialVersionUID = 1L;
	private final Quest quest;
	private final Player player;

	protected QuestEvent(String name, Quest quest, Player player) {
		super(name);
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
