package com.citizens.NPCTypes.Questers.Quests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.citizens.NPCTypes.Questers.Objectives.Objective;
import com.citizens.NPCTypes.Questers.Objectives.Objective.Progress;
import com.citizens.NPCTypes.Questers.Objectives.Objectives.ObjectiveCycler;
import com.citizens.Resources.NPClib.HumanNPC;

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
		this.objective = objectives.current().current();
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