package com.citizens.npctypes.questers.quests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;

import com.citizens.npctypes.questers.objectives.Objective;
import com.citizens.npctypes.questers.objectives.Objective.Progress;
import com.citizens.npctypes.questers.objectives.Objectives.ObjectiveCycler;
import com.citizens.resources.npclib.HumanNPC;

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

	public abstract Type[] getEventTypes();
}