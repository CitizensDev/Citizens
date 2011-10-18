package net.citizensnpcs.questers.rewards;

import org.bukkit.entity.Player;

public interface Requirement extends Reward {
	public boolean fulfilsRequirement(Player player);

	public String getRequiredText(Player player);
}
