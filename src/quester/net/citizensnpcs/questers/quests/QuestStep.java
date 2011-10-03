package net.citizensnpcs.questers.quests;

import java.util.Collections;
import java.util.List;

import net.citizensnpcs.questers.quests.progress.QuestProgress;

import org.bukkit.entity.Player;

public class QuestStep {
	private final List<Objective> objectives;
	private final RewardGranter granter;

	public QuestStep(List<Objective> objectives, RewardGranter granter) {
		this.objectives = objectives;
		this.granter = granter;
	}

	public void onCompletion(Player player, QuestProgress progress) {
		this.granter.onCompletion(player, progress);
	}

	public List<Objective> objectives() {
		return Collections.unmodifiableList(objectives);
	}
}