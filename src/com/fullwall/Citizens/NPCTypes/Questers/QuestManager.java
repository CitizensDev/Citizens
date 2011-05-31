package com.fullwall.Citizens.NPCTypes.Questers;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class QuestManager {
	public enum QuestType {
		/**
		 * Place blocks
		 */
		BUILD,
		/**
		 * Deliver item(s) to an NPC
		 */
		DELIVERY,
		/**
		 * Break blocks
		 */
		DESTROY_BLOCK,
		/**
		 * Kill players
		 */
		PLAYER_COMBAT,
		/**
		 * Kill mobs
		 */
		HUNT,
		/**
		 * Travel a distance
		 */
		MOVE_DISTANCE,
		/**
		 * Travel to a location
		 */
		MOVE_LOCATION,
		/**
		 * Collect item(s)/blocks(s)
		 */
		COLLECT,
		/**
		 * Earn money
		 */
		EARN;
	}

	private static HashMap<String, PlayerProfile> cachedProfiles = new HashMap<String, PlayerProfile>();

	public static void load(Player player) {
		PlayerProfile profile = new PlayerProfile(player.getName());
		cachedProfiles.put(player.getName(), profile);
	}

	public static void unload(Player player) {
		getProfile(player.getName()).save();
		cachedProfiles.put(player.getName(), null);
	}

	public static void incrementQuest(Player player, Event event) {
		getProfile(player.getName()).getCurrentQuest().updateProgress(event);
	}

	public static boolean hasQuest(Player player) {
		return getProfile(player.getName()).hasQuest();
	}

	private static PlayerProfile getProfile(String name) {
		return cachedProfiles.get(name);
	}
}