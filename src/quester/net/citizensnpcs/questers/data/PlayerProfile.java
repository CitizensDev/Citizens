package net.citizensnpcs.questers.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.properties.ConfigurationHandler;
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
	private static final Map<String, PlayerProfile> profiles = new HashMap<String, PlayerProfile>();
	private long lastSave;

	private PlayerProfile(String name) {
		profile = new ConfigurationHandler("plugins/Citizens/profiles/" + name
				+ ".yml");
		this.name = name;
		this.load();
	}

	public static Collection<PlayerProfile> getOnline() {
		return profiles.values();
	}

	public static PlayerProfile getProfile(String name, boolean register) {
		if (profiles.get(name) == null) {
			PlayerProfile profile = new PlayerProfile(name);
			if (register) {
				profiles.put(name, profile);
			}
			return profile;
		}
		return profiles.get(name);
	}

	public static PlayerProfile getProfile(String name) {
		return getProfile(name, true);
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

	private final ConfigurationHandler profile;
	private final Map<String, CompletedQuest> completedQuests = Maps
			.newHashMap();
	private QuestProgress progress;
	private final String name;

	public void addCompletedQuest(CompletedQuest quest) {
		completedQuests.put(quest.getName().toLowerCase(), quest);
	}

	public void removeCompletedQuest(String name) {
		completedQuests.remove(name.toLowerCase());
	}

	public CompletedQuest getCompletedQuest(String name) {
		return completedQuests.get(name.toLowerCase());
	}

	public void removeAllCompletedQuests() {
		completedQuests.clear();
	}

	public Collection<CompletedQuest> getAllCompleted() {
		return Collections.unmodifiableCollection(completedQuests.values());
	}

	public boolean hasCompleted(String quest) {
		return completedQuests.containsKey(quest.toLowerCase());
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

	public String getQuest() {
		return progress == null ? "" : progress.getQuestName();
	}

	public boolean isOnline() {
		return Bukkit.getServer().getPlayer(name) != null;
	}

	public void save() {
		this.lastSave = System.currentTimeMillis();
		if (progress != null && progress.getProgress() != null) {
			if (profile.pathExists("quests.current"))
				profile.removeKey("quests.current");
			String path = "quests.current", oldPath = path;
			int count = 0;

			profile.setString(path + ".name", progress.getQuestName());
			profile.setInt(path + ".step", progress.getStep());
			profile.setLong(path + ".start-time", progress.getStartTime());
			profile.setInt(path + ".giver", progress.getQuesterUID());

			for (ObjectiveProgress current : progress.getProgress()) {
				path = oldPath + "." + count + ".progress";
				if (current == null) {
					++count;
					continue;
				}
				profile.setInt(path + ".amount", current.getAmount());
				if (current.getLastItem() != null) {
					profile.setInt(path + ".item.id", current.getLastItem()
							.getTypeId());
					profile.setInt(path + ".item.amount", current.getLastItem()
							.getAmount());
					profile.setInt(path + ".item.data", current.getLastItem()
							.getDurability());
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
			if (progress.getProgress() == null)
				break questLoad;
			int count = 0;
			for (ObjectiveProgress questProgress : progress.getProgress()) {
				temp = path + "." + count + ".progress";
				if (questProgress == null || !profile.pathExists(temp)) {
					progress.getProgress()[count] = null;
					++count;
					continue;
				}
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

	@Override
	public int hashCode() {
		return 31 + ((name == null) ? 0 : name.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		PlayerProfile other = (PlayerProfile) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	public long getLastSaveTime() {
		return lastSave;
	}
}