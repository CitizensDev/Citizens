package net.citizensnpcs.questers.quests.progress;

import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.quests.Objectives.ObjectiveCycler;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class QuestProgress {
	private ObjectiveProgress[] progress;
	private final String questName;
	private final ObjectiveCycler objectives;
	private final long startTime;
	private final int UID;
	private final Player player;

	public QuestProgress(int UID, Player player, String questName,
			long startTime) {
		this.UID = UID;
		this.player = player;
		this.questName = questName;
		this.objectives = QuestManager.getQuest(questName).getObjectives()
				.newCycler();
		this.startTime = startTime;
		addObjectives();
	}

	private void addObjectives() {
		if (objectives.current().objectives().size() == 0)
			return;
		int size = objectives.current().objectives().size();
		this.progress = new ObjectiveProgress[size];
		while (objectives.hasNext()) {
			this.progress[objectives.index()] = new ObjectiveProgress(UID,
					player, questName, objectives.nextObjective());
		}
	}

	public void cycle() {
		next();
		if (!this.objectives.isCompleted()) {
			addObjectives();
		}
	}

	public ObjectiveProgress[] getProgress() {
		return progress;
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

	public int getStep() {
		return this.objectives.currentStep();
	}

	public boolean isFullyCompleted() {
		return isStepCompleted() && this.objectives.isCompleted();
	}

	public boolean isStepCompleted() {
		if (progress == null)
			return true;
		for (ObjectiveProgress prog : progress) {
			if (prog != null && !prog.getObjective().isOptional()) {
				return false;
			}
		}
		return true;
	}

	private void next() {
		this.progress = null;
		this.objectives.cycle();
	}

	public void onStepCompletion() {
		this.objectives.current().onCompletion(player, this);
	}

	public void setStep(int step) {
		while (this.objectives.currentStep() != step) {
			cycle();
		}
	}

	public void updateProgress(Player player, Event event) {
		if (progress.length > 0) {
			int idx = 0;
			for (ObjectiveProgress progress : this.progress) {
				if (progress == null) {
					++idx;
					continue;
				}
				boolean cont = true;
				for (Event.Type type : progress.getEventTypes()) {
					if (type == event.getType()) {
						cont = false;
						break;
					}
				}
				if (cont) {
					++idx;
					continue;
				}
				if (progress.update(event)) {
					progress.getObjective().onCompletion(player, this);
					this.progress[idx] = null;
				}
				++idx;
			}
		}
	}
}