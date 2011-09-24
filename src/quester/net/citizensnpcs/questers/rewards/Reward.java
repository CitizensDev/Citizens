package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;

import org.bukkit.entity.Player;

public interface Reward {
	public void grant(Player player, int UID);

	public boolean canTake(Player player);

	public boolean isTake();

	public String getRequiredText(Player player);

	public void save(Storage storage, String root);
}