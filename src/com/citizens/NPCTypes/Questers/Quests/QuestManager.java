package com.citizens.NPCTypes.Questers.Quests;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import com.citizens.NPCTypes.Questers.Quest;
import com.citizens.Properties.PlayerProfile;
import com.citizens.Resources.NPClib.HumanNPC;

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
			String filtered = string.trim().toUpperCase();
			filtered = filtered.replaceAll("\\s+", "_").replaceAll("\\W", "");
			result = lookupNames.get(filtered);
			return result;
		}
	}

	public enum RewardType {
		HEALTH, ITEM, MONEY, PERMISSION, QUEST, RANK;
	}

	private static final HashMap<String, PlayerProfile> cachedProfiles = new HashMap<String, PlayerProfile>();
	private static final HashMap<String, Quest> quests = new HashMap<String, Quest>();

	public static void load(Player player) {
		PlayerProfile profile = new PlayerProfile(player.getName());
		cachedProfiles.put(player.getName(), profile);
	}

	public static void unload(Player player) {
		getProfile(player.getName()).save();
		cachedProfiles.put(player.getName(), null);
	}

	public static void incrementQuest(Player player, Event event) {
		if (event instanceof Cancellable && ((Cancellable) event).isCancelled())
			return;
		if (hasQuest(player)) {
			boolean completed = getProfile(player.getName()).getProgress()
					.updateProgress(event);
			if (completed) {
				QuestProgress progress = getProfile(player.getName())
						.getProgress();
				progress.cycle();
				// TODO Uncomment after BukkitContrib is updated to MC 1.7.2
				// Achievements.award(player, Achievement.QUEST_COMPLETE);
			}
		}
	}

	public static void initialize() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			load(player);
		}
	}

	public static boolean hasQuest(Player player) {
		return getProfile(player.getName()) == null ? false : getProfile(
				player.getName()).hasQuest();
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
		return getQuest(quest) != null;
	}

	public static void addQuest(Quest quest) {
		quests.put(quest.getName(), quest);
	}
}