package com.citizens.Misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkitcontrib.player.ContribPlayer;

import com.citizens.Properties.PlayerProfile;
import com.citizens.Properties.PropertyManager;

public class Achievements {
	public enum Achievement {
		EVIL_TAME("Misunderstood."),
		NPC_CREATE("Your own slave."),
		HEALER_REVIVE("Savior."),
		EVIL_KILL("Removed Herobrine.");

		private final String desc;

		Achievement(String desc) {
			this.desc = desc;
		}

		public String getDescription() {
			return this.desc;
		}
	}

	/**
	 * Send a custom Citizens achievement to the client using BukkitContrib
	 * 
	 * @param player
	 * @param achievement
	 * @param icon
	 */
	public static void award(Player player, Achievement achievement,
			Material icon) {
		PlayerProfile profile = PropertyManager.getPlayerProfile(player
				.getName());
		if (!profile.getAchievements().contains(achievement.name())) {
			profile.addAchievement(achievement.name());
			((ContribPlayer) player).sendNotification("Citizens Achievement",
					achievement.getDescription(), icon);
		}
	}
}