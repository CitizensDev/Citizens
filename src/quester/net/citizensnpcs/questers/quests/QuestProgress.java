package net.citizensnpcs.questers.quests;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.quests.Objectives.ObjectiveCycler;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class QuestProgress {
	private final List<ObjectiveProgress> progress = new ArrayList<ObjectiveProgress>();
	private final String questName;
	private final ObjectiveCycler objectives;
	private long startTime;
	private final int UID;
	private final Player player;

	public QuestProgress(int UID, Player player, String questName) {
		this.UID = UID;
		this.player = player;
		this.questName = questName;
		this.objectives = getObjectives().newCycler();
		for (int i = 0; i != objectives.current().objectives().size(); ++i) {
			this.progress.add(new ObjectiveProgress(UID, player, questName,
					objectives));
		}
		this.setStartTime(System.currentTimeMillis());
	}

	public void cycle() {
		for (Objective objective : this.objectives.current().objectives()) {
			String message = objective.getMessage();
			if (!message.isEmpty())
				Messaging.send(player, message);
		}
		this.progress.clear();
		if (!this.objectives.isCompleted()) {
			next();
		} else {
			this.objectives.cycle();
		}
	}

	private void next() {
		this.objectives.cycle();
		for (int i = 0; i != objectives.current().objectives().size(); ++i) {
			this.progress.add(new ObjectiveProgress(UID, player, questName,
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

	public void updateProgress(Event event) {
		if (progress.size() > 0) {
			for (ObjectiveProgress progress : this.progress) {
				boolean cont = true;
				for (Event.Type type : progress.getEventTypes()) {
					if (type == event.getType()) {
						cont = false;
						break;
					}
				}
				if (cont || progress.isCompleted())
					continue;
				progress.update(event);
			}
		}
	}

	public int getQuesterUID() {
		return this.UID;
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