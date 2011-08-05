package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.quests.QuestManager.RewardType;

import org.bukkit.entity.Player;

public class HealthReward implements Reward {
	private final int reward;
	private final boolean take;

	public HealthReward(int reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player) {
		if (this.take)
			player.setHealth(player.getHealth() - reward);
		else
			player.setHealth(player.getHealth() + reward);
	}

	@Override
	public RewardType getType() {
		return RewardType.HEALTH;
	}

	@Override
	public Object getReward() {
		return reward;
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean canTake(Player player) {
		return take ? player.getHealth() - reward > 0 : true;
	}
}