package net.citizensnpcs.questers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.questers.quests.Quest;
import net.citizensnpcs.questers.quests.QuestProgress;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class QuestManager {
	private static final Map<String, Quest> quests = new HashMap<String, Quest>();

	public static void unload(Player player) {
		if (getProfile(player.getName()) != null)
			getProfile(player.getName()).save();
		PlayerProfile.setProfile(player.getName(), null);
	}

	public static void incrementQuest(Player player, Event event) {
		if (event == null
				|| (event instanceof Cancellable && ((Cancellable) event)
						.isCancelled()))
			return;
		if (hasQuest(player)) {
			QuestProgress progress = getProfile(player.getName()).getProgress();
			if (progress.isFullyCompleted())
				return;
			progress.updateProgress(player, event);
			if (progress.isStepCompleted()) {
				progress.cycle();
			}
		}
	}

	public static boolean hasQuest(Player player) {
		return getProfile(player.getName()).hasQuest();
	}

	private static PlayerProfile getProfile(String string) {
		return PlayerProfile.getProfile(string);
	}

	public static Quest getQuest(String questName) {
		return quests.get(questName.toLowerCase());
	}

	public static void assignQuest(HumanNPC npc, Player player, String quest) {
		getProfile(player.getName()).setProgress(
				new QuestProgress(npc.getUID(), player, quest, System
						.currentTimeMillis()));
	}

	public static boolean validQuest(String quest) {
		return getQuest(quest) != null;
	}

	public static void addQuest(Quest quest) {
		quests.put(quest.getName().toLowerCase(), quest);
	}

	public static Collection<Quest> quests() {
		return quests.values();
	}
}