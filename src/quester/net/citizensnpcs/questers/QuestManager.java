package net.citizensnpcs.questers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.questers.api.events.QuestBeginEvent;
import net.citizensnpcs.questers.data.PlayerProfile;
import net.citizensnpcs.questers.quests.Quest;
import net.citizensnpcs.questers.quests.progress.QuestProgress;
import net.citizensnpcs.questers.rewards.Reward;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

	public static boolean canRepeat(Player player, Quest quest) {
		if (quest == null) {
			return false;
		}
		PlayerProfile profile = PlayerProfile.getProfile(player.getName());
		return !profile.hasCompleted(quest.getName())
				|| (quest.getRepeatLimit() == -1 || profile.getCompletedQuest(
						quest.getName()).getTimesCompleted() < quest
						.getRepeatLimit());
	}

	public static boolean assignQuest(Player player, int UID, String q) {
		q = q.toLowerCase();
		if (!isValidQuest(q)) {
			throw new IllegalArgumentException("Given quest does not exist");
		}
		Quest quest = quests.get(q);
		if (!canRepeat(player, quest)) {
			player.sendMessage(ChatColor.GRAY
					+ "You are not allowed to repeat this quest again.");
			return false;
		}
		for (Reward requirement : quest.getRequirements()) {
			if (!requirement.canTake(player)) {
				player.sendMessage(ChatColor.GRAY + "Missing requirement. "
						+ requirement.getRequiredText(player));
				return false;
			}
			requirement.grant(player, UID);
		}
		QuestBeginEvent call;
		Bukkit.getPluginManager().callEvent(
				(call = new QuestBeginEvent(quest, player)));
		if (call.isCancelled()) {
			return false;
		}
		getProfile(player.getName()).setProgress(
				new QuestProgress(UID, player, q, System.currentTimeMillis()));
		Messaging.send(player, quest.getAcceptanceText());
		return true;
	}

	public static boolean isValidQuest(String quest) {
		return getQuest(quest) != null;
	}

	public static void addQuest(Quest quest) {
		quests.put(quest.getName().toLowerCase(), quest);
	}

	public static Collection<Quest> quests() {
		return quests.values();
	}
}