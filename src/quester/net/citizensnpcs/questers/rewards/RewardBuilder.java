package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.DataKey;

public interface RewardBuilder {
	public Reward build(DataKey root, boolean take);
}