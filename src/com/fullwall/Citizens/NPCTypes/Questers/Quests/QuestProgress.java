package com.fullwall.Citizens.NPCTypes.Questers.Quests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager.QuestType;
import com.fullwall.Citizens.NPCTypes.Questers.Objectives.Objectives;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuestProgress {
	private QuestIncrementer incrementer;
	private final String questName;

	public QuestProgress(HumanNPC npc, Player player, String questName,
			QuestType type) {
		this.questName = questName;
		this.incrementer = QuestFactory.createIncrementer(npc, player,
				questName, type);
	}

	public void cycle(QuestType type) {
		if (!getObjectives().isCompleted()) {
			getObjectives().cycle();
			this.incrementer = QuestFactory.createIncrementer(
					incrementer.getQuester(), incrementer.getPlayer(),
					questName, type);
		}
	}

	public Objectives getObjectives() {
		return QuestManager.getQuest(this.questName).getObjectives();
	}

	public void updateProgress(Event event) {
		if (incrementer != null)
			this.incrementer.updateProgress(event);
	}
}
