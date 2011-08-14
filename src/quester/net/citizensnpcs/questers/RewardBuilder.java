package net.citizensnpcs.questers;

import net.citizensnpcs.properties.Storage;

public interface RewardBuilder {
	public Reward build(Storage storage, String root, boolean take);
}