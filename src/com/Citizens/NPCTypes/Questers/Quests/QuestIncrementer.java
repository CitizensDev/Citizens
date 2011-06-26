package com.Citizens.NPCTypes.Questers.Quests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.Citizens.resources.redecouverte.NPClib.HumanNPC;
import com.Citizens.NPCTypes.Questers.Objectives.Objective;
import com.Citizens.NPCTypes.Questers.Objectives.Objective.Progress;
import com.Citizens.NPCTypes.Questers.Objectives.Objectives.ObjectiveCycler;

public abstract class QuestIncrementer {
	private final HumanNPC quester;
	protected final Objective objective;
	protected final Player player;
	protected final String questName;

	protected QuestIncrementer(HumanNPC npc, Player player, String questName,
			ObjectiveCycler objectives) {
		this.quester = npc;
		this.player = player;
		this.questName = questName;
		this.objective = objectives.current();
	}

	public HumanNPC getQuester() {
		return quester;
	}

	public Player getPlayer() {
		return player;
	}

	public Progress getProgress() {
		return this.objective.getProgress();
	}

	public abstract boolean isCompleted();

	public abstract void updateProgress(Event event);
}