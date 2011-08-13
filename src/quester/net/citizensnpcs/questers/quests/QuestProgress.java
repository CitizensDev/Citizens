package net.citizensnpcs.questers.quests;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.questers.quests.Objectives.ObjectiveCycler;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class QuestProgress {
	private final List<ObjectiveProgress> progress = new ArrayList<ObjectiveProgress>();
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
			this.progress.add(new ObjectiveProgress(npc, player, questName,
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
			this.progress.add(new ObjectiveProgress(npc, player, questName,
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

	public boolean stepCompleted() {
		for (ObjectiveProgress prog : progress) {
			if (!prog.isCompleted()) {
				return false;
			}
		}
		return true;
	}

	public boolean fullyCompleted() {
		return stepCompleted() && this.objectives.isCompleted();
	}

	public boolean updateProgress(Event event) {
		boolean completed = true;
		if (progress.size() > 0) {
			for (ObjectiveProgress progress : this.progress) {
				if (!progress.update(event))
					completed = false;
			}
		}
		return completed;
	}

	public int getQuesterUID() {
		return this.npc.getUID();
	}

	public String getQuestName() {
		return questName;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public List<ObjectiveProgress> getProgress() {
		return progress;
	}

	public ObjectiveProgress getProgress(int count) {
		return progress.get(count);
	}

}
