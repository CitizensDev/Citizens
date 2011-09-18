package net.citizensnpcs.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerUtils {

	// Get a player object from the provided name
	public static Player matchPlayer(String name) {
		List<Player> players = Bukkit.getServer().matchPlayer(name);
		if (!players.isEmpty()) {
			if (players.get(0) != null) {
				return players.get(0);
			}
		}
		return null;
	}
}