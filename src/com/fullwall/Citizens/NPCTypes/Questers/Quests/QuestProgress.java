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
				Messaging.send(this.incrementer.player, message);
			this.objectives.cycle();
			this.incrementer = QuestFactory.createIncrementer(
					incrementer.getQuester(), incrementer.getPlayer(),
					getQuestName(), this.objectives.current().getType(),
					this.objectives);
		}
	}

	public Objectives getObjectives() {
		return QuestManager.getQuest(this.getQuestName()).getObjectives();
	}

	public boolean fullyCompleted() {
		return this.objectives.isCompleted();
	}

	public int getQuesterUID() {
		return this.incrementer.getQuester().getUID();
	}

	public boolean updateProgress(Event event) {
		if (incrementer != null)
			this.incrementer.updateProgress(event);
		return this.incrementer.isCompleted();
	}

	public String getQuestName() {
		return questName;
	}
}
