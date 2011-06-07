package com.fullwall.Citizens.NPCTypes.Questers.Quests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.NPCTypes.Questers.Objectives.Objective;
import com.fullwall.Citizens.NPCTypes.Questers.Objectives.Objective.Progress;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public abstract class QuestIncrementer {
	private final HumanNPC quester;
	protected Objective objective;
	protected final Player player;
	protected final String questName;

	protected QuestIncrementer(HumanNPC npc, Player player, String questName) {
		this.quester = npc;
		this.player = player;
		this.questName = questName;
		this.objective = QuestManager.getQuest(this.questName).getObjectives()
				.current();
	}

	public HumanNPC getQuester() {
		return quester;
	}

	public Player getPlayer() {
		return player;
	}

	protected Progress getProgress() {
		return this.objective.getProgress();
	}

	public abstract boolean isCompleted();

	public abstract void updateProgress(Event event);

}
