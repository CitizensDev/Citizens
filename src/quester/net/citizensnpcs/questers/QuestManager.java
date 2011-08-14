package net.citizensnpcs.questers;

import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.questers.quests.Quest;
import net.citizensnpcs.questers.quests.QuestProgress;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class QuestManager {
	public enum RewardType {
		HEALTH,
		ITEM,
		MONEY,
		PERMISSION,
		QUEST,
		RANK;
	}

	private static final Map<String, PlayerProfile> cachedProfiles = new HashMap<String, PlayerProfile>();
	private static final Map<String, Quest> quests = new HashMap<String, Quest>();

	public static void load(Player player) {
		PlayerProfile profile = new PlayerProfile(player.getName());
		cachedProfiles.put(player.getName(), profile);
	}

	public static void unload(Player player) {
		if (getProfile(player.getName()) != null)
			getProfile(player.getName()).save();
		cachedProfiles.put(player.getName(), null);
	}

	public static void incrementQuest(Player player, Event event) {
		if (event instanceof Cancellable && ((Cancellable) event).isCancelled())
			return;
		if (hasQuest(player)) {
			QuestProgress progress = getProfile(player.getName()).getProgress();
			progress.updateProgress(event);
			if (progress.stepCompleted()) {
				progress.cycle();
			}
		}
	}

	public static void initialize() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			load(player);
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