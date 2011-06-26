package com.citizens.NPCTypes.Questers.Quests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.citizens.NPCTypes.Questers.Objectives.Objectives;
import com.citizens.NPCTypes.Questers.Objectives.Objectives.ObjectiveCycler;
import com.citizens.NPCTypes.Questers.Quests.QuestFactory;
import com.citizens.NPCTypes.Questers.Quests.QuestIncrementer;
import com.citizens.NPCTypes.Questers.Quests.QuestManager;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;
import com.iConomy.util.Messaging;

public class QuestProgress {
	private QuestIncrementer incrementer;
	private final String questName;
	private final ObjectiveCycler objectives;
	private long startTime;

	public QuestProgress(HumanNPC npc, Player player, String questName) {
		this.setStartTime(System.currentTimeMillis());
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

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() {
		return startTime;
	}
}
