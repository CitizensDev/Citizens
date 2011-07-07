package com.citizens.misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkitcontrib.player.ContribPlayer;

import com.citizens.properties.PlayerProfile;
import com.citizens.properties.PropertyManager;

public class Achievements {
	public enum Achievement {
		EVIL_KILL("Removed Herobrine.", Material.DIAMOND_SWORD),
		EVIL_TAME("Misunderstood.", Material.CAKE),
		HEALER_REVIVE("Savior.", Material.DIAMOND_BLOCK),
		NPC_CREATE("Your very own slave.", Material.SADDLE),
		QUEST_COMPLETE("Quest complete!", Material.GOLD_INGOT);

		private final String desc;
		private final Material icon;

		Achievement(String desc, Material icon) {
			this.desc = desc;
			this.icon = icon;
		}

		public String getDescription() {
			return this.desc;
		}

		public Material getIcon() {
			return this.icon;
		}
	}

	/**
	 * Send a custom Citizens achievement to the client using BukkitContrib
	 * 
	 * @param player
	 * @param achievement
	 */
	public static void award(Player player, Achievement achievement) {
		PlayerProfile profile = PropertyManager.getPlayerProfile(player
				.getName());
		if (!profile.getAchievements().contains(achievement.name())) {
			profile.addAchievement(achievement.name());
			((ContribPlayer) player).sendNotification("Citizens Achievement",
					achievement.getDescription(), achievement.getIcon());
		}
	}
}