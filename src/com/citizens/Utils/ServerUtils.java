package com.citizens.Utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkitcontrib.player.ContribPlayer;

import com.citizens.Properties.PlayerProfile;
import com.citizens.Properties.PropertyManager;

public class ServerUtils {

	/**
	 * Get a player object from the provided name
	 * 
	 * @param name
	 * @return
	 */
	public static Player matchPlayer(String name) {
		List<Player> players = Bukkit.getServer().matchPlayer(name);
		if (!players.isEmpty()) {
			if (players.get(0) != null) {
				return players.get(0);
			}
		}
		return null;
	}

	/**
	 * Send a custom Citizens achievement to the client using BukkitContrib
	 * 
	 * @param player
	 * @param achievement
	 * @param icon
	 */
	public static void sendAchievement(Player player, String achievement,
			Material icon) {
		PlayerProfile profile = PropertyManager.getPlayerProfile(player
				.getName());
		if (!profile.getAchievements().contains(achievement)) {
			profile.addAchievement(achievement);
			((ContribPlayer) player).sendNotification("Citizens Achievement",
					achievement, icon);
		}
	}
}