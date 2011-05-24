package com.fullwall.Citizens.NPCTypes.Questers;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class QuestManager {
	public enum QuestType {
		BUILD, DELIVERY, DESTROY_BLOCK, PLAYER_COMBAT, HUNT, MOVE_DISTANCE, MOVE_LOCATION, COLLECT, EARN;
	}

	private static HashMap<String, PlayerProfile> cachedProfiles = new HashMap<String, PlayerProfile>();

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