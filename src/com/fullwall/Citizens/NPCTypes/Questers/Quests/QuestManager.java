package com.fullwall.Citizens.NPCTypes.Questers.Quests;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.fullwall.Citizens.NPCTypes.Questers.PlayerProfile;
import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuestManager {
	public enum QuestType {
		/**
		 * Place blocks
		 */
		BUILD,
		/**
		 * Collect item(s)/blocks(s)
		 */
		COLLECT,
		/**
		 * Deliver item(s) to an NPC
		 */
		DELIVERY,
		/**
		 * Break blocks
		 */
		DESTROY_BLOCK,
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
		 * Kill players
		 */
		PLAYER_COMBAT;
		private final static Map<String, QuestType> lookupNames = new HashMap<String, QuestType>();
		static {
			for (QuestType type : QuestType.values()) {
				lookupNames.put(type.name(), type);
			}
		}

		public static QuestType getType(String string) {
			QuestType result = null;
			String filtered = string.toUpperCase();
			filtered = filtered.replaceAll("\\s+", "_").replaceAll("\\W", "");
			result = lookupNames.get(filtered);
			return result;
		}
	}

	public enum RewardType {
		HEALTH,
		ITEM,
		MONEY,
		PERMISSION,
		QUEST,
		RANK;
	}

	private static HashMap<String, PlayerProfile> cachedProfiles = new HashMap<String, PlayerProfile>();
	private static HashMap<String, Quest> quests = new HashMap<String, Quest>();

	public static void load(Player player) {
		PlayerProfile profile = new PlayerProfile(player.getName());
		cachedProfiles.put(player.getName(), profile);
	}

	public static void unload(Player player) {
		getProfile(player.getName()).save();
		cachedProfiles.put(player.getName(), null);
	}

	public static void incrementQuest(Player player, Event event) {
		if (hasQuest(player)) {
			boolean completed = getProfile(player.getName()).getProgress()
					.updateProgress(event);
			if (completed) {
				QuestProgress progress = getProfile(player.getName())
						.getProgress();
				progress.cycle();
			}
		}
	}

	public static boolean hasQuest(Player player) {
		return getProfile(player.getName()).hasQuest();
	}

	public static PlayerProfile getProfile(String name) {
		return cachedProfiles.get(name);
	}

	public static Quest getQuest(String questName) {
		return quests.get(questName);
	}

	public static void assignQuest(HumanNPC npc, Player player, String quest) {
		PlayerProfile profile = getProfile(player.getName());
		profile.setProgress(new QuestProgress(npc, player, quest));
		setProfile(player.getName(), profile);
	}

	public static void setProfile(String name, PlayerProfile profile) {
		cachedProfiles.put(name, profile);
	}

	public static boolean validQuest(String quest) {
		return getQuest(quest) == null;
	}
}