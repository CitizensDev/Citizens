package com.citizens.npctypes.questers.quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.citizens.npctypes.questers.objectives.Objective;
import com.citizens.npctypes.questers.objectives.Objectives;
import com.citizens.npctypes.questers.objectives.Objectives.ObjectiveCycler;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.Messaging;

public class QuestProgress {
	private final List<QuestIncrementer> incrementers = new ArrayList<QuestIncrementer>();
	private final String questName;
	private final ObjectiveCycler objectives;
	private long startTime;
	private final HumanNPC npc;
	private final Player player;

	public QuestProgress(HumanNPC npc, Player player, String questName) {
		this.npc = npc;
		this.player = player;
		this.setStartTime(System.currentTimeMillis());
		this.questName = questName;
		this.objectives = getObjectives().newCycler();
		for (Objective objective : objectives.current().all()) {
			this.incrementers.add(QuestFactory.createIncrementer(npc, player,
					questName, objective.getType(), objectives));
		}
	}

	public void cycle() {
		if (!this.objectives.isCompleted()) {
			for (Objective objective : this.objectives.current().all()) {
				String message = objective.getMessage();
				if (!message.isEmpty())
					Messaging.send(player, message);
				next();
			}
		}
	}

	private void next() {
		this.objectives.cycle();
		for (Objective objective : objectives.current().all()) {
			this.incrementers.add(QuestFactory.createIncrementer(npc, player,
					questName, objective.getType(), objectives));
		}
	}

	public int getStep() {
		return this.objectives.currentStep();
	}

	public void setStep(int step) {
		while (this.objectives.currentStep() != step) {
			next();
		}
	}

	public Objectives getObjectives() {
		return QuestManager.getQuest(this.getQuestName()).getObjectives();
	}

	public boolean fullyCompleted() {
		return this.objectives.isCompleted();
	}

	public int getQuesterUID() {
		return npc.getUID();
	}

	public boolean updateProgress(Event event) {
		boolean completed = true;
		if (incrementers.size() > 0) {
			for (QuestIncrementer incrementer : this.incrementers) {
				incrementer.updateProgress(event);
				if (!incrementer.isCompleted())
					completed = false;
			}
		}
		return completed;
	}

	public String getQuestName() {
		return questName;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public List<QuestIncrementer> getIncrementers() {
		return incrementers;
	}

	public QuestIncrementer getIncrementer(int count) {
		return incrementers.get(count);
	}
}
