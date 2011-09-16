package net.citizensnpcs.questers.quests.progress;

import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.quests.Objectives;
import net.citizensnpcs.questers.quests.Objectives.ObjectiveCycler;
import net.citizensnpcs.utils.Messaging;

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
		this.objectives = getObjectives().newCycler();
		this.startTime = startTime;
		addObjectives();
	}

	public void cycle() {
		next();
		if (!this.objectives.isCompleted()) {
			addObjectives();
		}
	}

	private void next() {
		this.progress = null;
		this.objectives.cycle();
	}

	private void addObjectives() {
		int size = objectives.current().objectives().size();
		this.progress = new ObjectiveProgress[size];
		for (int i = 0; i != size; ++i) {
			this.progress[i] = new ObjectiveProgress(UID, player, questName,
					objectives);
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

	public boolean isStepCompleted() {
		if (progress == null)
			return true;
		for (ObjectiveProgress prog : progress) {
			if (prog != null) {
				return false;
			}
		}
		return true;
	}

	public boolean isFullyCompleted() {
		return isStepCompleted() && this.objectives.isCompleted();
	}

	public void updateProgress(Player player, Event event) {
		if (progress.length > 0) {
			int idx = -1;
			for (ObjectiveProgress progress : this.progress) {
				++idx;
				if (progress == null || progress.isCompleted())
					continue;
				boolean cont = true;
				for (Event.Type type : progress.getEventTypes()) {
					if (type == event.getType()) {
						cont = false;
						break;
					}
				}
				if (cont)
					continue;
				if (progress.update(event)) {
					this.progress[idx] = null;
					if (!progress.getObjective().getMessage().isEmpty()) {
						Messaging.send(player, progress.getObjective()
								.getMessage());
					}
				}
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

	public ObjectiveProgress[] getProgress() {
		return progress;
	}
}