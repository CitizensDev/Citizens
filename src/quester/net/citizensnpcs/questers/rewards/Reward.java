package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;

import org.bukkit.entity.Player;

public interface Reward {
	public void grant(Player player, int UID);

	public boolean isTake();

	public void save(Storage storage, String root);
}