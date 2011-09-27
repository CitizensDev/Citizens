package net.citizensnpcs.questers.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.quests.CompletedQuest;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestProgress;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.google.common.collect.Maps;

public class PlayerProfile {
	private final ConfigurationHandler profile;
	private final Map<String, CompletedQuest> completedQuests = Maps
			.newHashMap();
	private QuestProgress progress;
	private final String name;

	private static final Map<String, PlayerProfile> profiles = new HashMap<String, PlayerProfile>();

	public PlayerProfile(String name) {
		profile = new ConfigurationHandler("plugins/Citizens/profiles/" + name
				+ ".yml");
		this.name = name;
		this.load();
	}

	public static PlayerProfile getProfile(String name) {
		if (profiles.get(name) == null) {
			profiles.put(name, new PlayerProfile(name));
		}
		return profiles.get(name);
	}

	public static void setProfile(String name, PlayerProfile profile) {
		if (profile == null) {
			profiles.remove(name);
		} else {
			profiles.put(name, profile);
		}
	}

	public static void saveAll() {
		for (PlayerProfile profile : profiles.values()) {
			profile.save();
		}
	}

	public void addCompletedQuest(CompletedQuest quest) {
		completedQuests.put(quest.getName(), quest);
	}

	public CompletedQuest getCompletedQuest(String name) {
		return completedQuests.get(name);
	}

	public Collection<CompletedQuest> getAllCompleted() {
		return completedQuests.values();
	}

	public boolean hasCompleted(String quest) {
		return completedQuests.containsKey(quest);
	}

	public QuestProgress getProgress() {
		return progress;
	}

	public void setProgress(QuestProgress progress) {
		this.progress = progress;
		if (this.profile.pathExists("quests.current")) {
			this.profile.removeKey("quests.current");
		}
	}

	public boolean hasQuest() {
		return progress != null;
	}

	public Storage getStorage() {
		return profile;
	}

	public void save() {
		if (progress != null) {
			String path = "quests.current", oldPath = path;
			int count = 0;

			profile.setString(path + ".name", progress.getQuestName());
			profile.setInt(path + ".step", progress.getStep());
			profile.setLong(path + ".start-time", progress.getStartTime());
			profile.setInt(path + ".giver", progress.getQuesterUID());

			for (ObjectiveProgress current : progress.getProgress()) {
				path = oldPath + "." + count + ".progress";
				profile.setInt(path + ".amount", current.getAmount());
				if (current.getLastItem() != null) {
					profile.setInt(path + ".item.id", current.getLastItem()
							.getTypeId());
					profile.setInt(path + ".item.amount", current.getLastItem()
							.getAmount());
					profile.setInt(path + ".item.id", current.getLastItem()
							.getData() == null ? 0 : current.getLastItem()
							.getData().getData());
				}
				if (current.getLastLocation() != null) {
					LocationUtils.saveLocation(profile,
							current.getLastLocation(), path, true);
				}
				++count;
			}
		}
		String path = "quests.completed.", temp;
		for (CompletedQuest quest : this.completedQuests.values()) {
			temp = path + quest.getName();
			profile.setInt(temp + ".completed", quest.getTimesCompleted());
			profile.setLong(temp + ".elapsed", quest.getElapsed());
			profile.setInt(temp + ".quester", quest.getQuesterUID());
		}
		profile.save();
	}

	private void load() {
		String path = "quests.current", temp = path;
		questLoad: if (!profile.getString(path + ".name").isEmpty()) {
			if (QuestManager.getQuest(profile.getString(path + ".name")) == null) {
				Bukkit.getServer()
						.getPlayer(name)
						.sendMessage(
								ChatColor.GRAY
										+ "Previous in-progress quest "
										+ StringUtils.wrap(
												profile.getString(path
														+ ".name"),
												ChatColor.GRAY)
										+ " no longer exists and has been aborted.");
				profile.removeKey("quests.current");
				break questLoad;
			}
			progress = new QuestProgress(profile.getInt(path + ".giver"),
					Bukkit.getServer().getPlayer(name), profile.getString(path
							+ ".name"), profile.getLong(path + ".start-time"));
			progress.setStep(profile.getInt(path + ".step"));
			int count = 0;
			for (ObjectiveProgress questProgress : progress.getProgress()) {
				if (questProgress == null)
					continue;
				temp = path + "." + count + ".progress";
				questProgress.setAmountCompleted(this.profile.getInt(temp
						+ ".amount"));
				int itemID = profile.getInt(temp + ".item.id");
				int amount = profile.getInt(temp + ".item.amount");
				if (itemID != 0 && amount > 0) {
					ItemStack item = null;
					item = new ItemStack(itemID, amount);
					item.setData(new MaterialData(itemID, (byte) profile
							.getInt(temp + ".item.data")));
					questProgress.setLastItem(item);
				}
				if (profile.pathExists(temp + ".location")) {
					questProgress.setLastLocation(LocationUtils.loadLocation(
							profile, temp, true));
				}
				++count;
			}
		}
		if (!profile.pathExists("quests.completed")) {
			return;
		}
		for (String key : profile.getKeys("quests.completed")) {
			path = "quests.completed." + key;
			completedQuests.put(
					key,
					new CompletedQuest(key, profile.getInt(path + ".quester"),
							profile.getInt(path + ".completed"), profile
									.getLong(path + ".elapsed")));
		}
	}
}