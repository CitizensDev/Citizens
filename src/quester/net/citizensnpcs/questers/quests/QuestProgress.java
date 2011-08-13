package net.citizensnpcs.questers.quests;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.questers.quests.Objectives.ObjectiveCycler;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class QuestProgress {
	private final List<ObjectiveProgress> progresss = new ArrayList<ObjectiveProgress>();
	private final String questName;
	private final ObjectiveCycler objectives;
	private long startTime;
	private final HumanNPC npc;
	private final Player player;

	public QuestProgress(HumanNPC npc, Player player, String questName) {
		this.npc = npc;
		this.player = player;
		this.questName = questName;
		this.objectives = getObjectives().newCycler();
		for (int i = 0; i != objectives.current().all().size(); ++i) {
			this.progresss.add(new ObjectiveProgress(npc, player, questName,
					objectives));
		}
		this.setStartTime(System.currentTimeMillis());
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
		for (int i = 0; i != objectives.current().all().size(); ++i) {
			this.progresss.add(new ObjectiveProgress(npc, player, questName,
					objectives));
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
		if (progresss.size() > 0) {
			for (ObjectiveProgress progress : this.progresss) {
				if (!progress.update(event))
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

	public List<ObjectiveProgress> getProgress() {
		return progresss;
	}

	public ObjectiveProgress getProgress(int count) {
		return progresss.get(count);
	}
}
