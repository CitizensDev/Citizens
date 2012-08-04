package net.citizensnpcs.questers.quests;

import java.util.Collections;
import java.util.List;

import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.quests.progress.QuestProgress;

import org.bukkit.entity.Player;

public class QuestStep {
	private final boolean finishHere;
	private final RewardGranter granter;
	private final List<Objective> objectives;

	public QuestStep(List<Objective> objectives, RewardGranter granter,
			boolean finishHere) {
		this.objectives = objectives;
		this.granter = granter;
		this.finishHere = finishHere;
	}

	public List<Objective> objectives() {
		return Collections.unmodifiableList(objectives);
	}

	public void onCompletion(Player player, QuestProgress progress) {
		this.granter.onCompletion(player, progress);
		if (this.finishHere)
			QuestManager.completeQuest(player);
	}
}