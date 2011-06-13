package com.fullwall.Citizens.NPCTypes.Questers.Quests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.NPCTypes.Questers.Objectives.Objectives;
import com.fullwall.Citizens.NPCTypes.Questers.Objectives.Objectives.ObjectiveCycler;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.iConomy.util.Messaging;

public class QuestProgress {
	private QuestIncrementer incrementer;
	private final String questName;
	private final ObjectiveCycler objectives;

	public QuestProgress(HumanNPC npc, Player player, String questName) {
		this.questName = questName;
		this.objectives = getObjectives().newCycler();
		this.incrementer = QuestFactory.createIncrementer(npc, player,
				questName, objectives.current().getType(), objectives);
	}

	public void cycle() {
		if (!this.objectives.isCompleted()) {
			String message = this.objectives.current().getMessage();
			if (!message.isEmpty())
				Messaging.send(this.getIncrementer().player, message);
			next();
		}
	}

	private void next() {
		this.objectives.cycle();
		this.incrementer = QuestFactory.createIncrementer(getIncrementer()
				.getQuester(), getIncrementer().getPlayer(), getQuestName(),
				this.objectives.current().getType(), this.objectives);
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
		return this.getIncrementer().getQuester().getUID();
	}

	public boolean updateProgress(Event event) {
		if (getIncrementer() != null)
			this.getIncrementer().updateProgress(event);
		return this.getIncrementer().isCompleted();
	}

	public String getQuestName() {
		return questName;
	}

	public QuestIncrementer getIncrementer() {
		return incrementer;
	}
}
