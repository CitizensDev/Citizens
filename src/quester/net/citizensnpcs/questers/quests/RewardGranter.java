package net.citizensnpcs.questers.quests;

import java.util.List;

import net.citizensnpcs.questers.quests.progress.QuestProgress;
import net.citizensnpcs.questers.rewards.Reward;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.entity.Player;

public class RewardGranter {
	// TODO: think of a better name for this.
	private final String completionMessage;
	private final List<Reward> rewards;

	public RewardGranter(String message, List<Reward> rewards) {
		this.completionMessage = message;
		this.rewards = rewards;
	}

	public String getCompletionMessage() {
		return this.completionMessage;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public void onCompletion(final Player player, final QuestProgress progress) {
		if (!this.completionMessage.isEmpty()) {
			Messaging.send(player, completionMessage);
		}
		Messaging.delay(new Runnable() {
			@Override
			public void run() {
				for (Reward reward : rewards) {
					reward.grant(player, progress.getQuesterUID());
				}
			}
		}, completionMessage);
	}
}
