package net.citizensnpcs.questers.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.properties.DataSource;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.quests.CompletedQuest;
import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.questers.quests.progress.QuestProgress;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

public class PlayerProfile {
	private PlayerProfile(String name) {
		profile = new ConfigurationHandler("plugins/Citizens/profiles/" + name
				+ ".yml");
		this.name = name;
		this.load();
	}

	public int getCompletedTimes(String reward) {
		return hasCompleted(reward) ? getCompletedQuest(reward)
				.getTimesCompleted() : 0;
	}

	private long lastSave;
	private final DataSource profile;
	private final Map<String, CompletedQuest> completedQuests = Maps
			.newHashMap();
	private final String name;
	private QuestProgress progress;

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
		DataKey root = profile.getKey("quests");
		root.removeKey("current");
		if (progress != null) {
			root = root.getRelative("current");
			int count = 0;

			root.setString("name", progress.getQuestName());
			root.setInt("step", progress.getStep());
			root.setLong("start-time", progress.getStartTime());
			root.setInt("giver", progress.getQuesterUID());
			DataKey parent;
			if (progress.getProgress() != null) {
				for (ObjectiveProgress current : progress.getProgress()) {
					parent = root.getRelative(count + ".progress");
					if (current == null) {
						++count;
						continue;
					}
					parent.setInt("amount", current.getAmount());
					if (current.getLastItem() != null) {
						parent.setInt("item.id", current.getLastItem()
								.getTypeId());
						parent.setInt("item.amount", current.getLastItem()
								.getAmount());
						parent.setInt("item.data", current.getLastItem()
								.getDurability());
					}
					if (current.getLastLocation() != null) {
						LocationUtils.saveLocation(parent,
								current.getLastLocation(), true);
					}
					++count;
				}
			}
		}
		profile.getKey("quests").removeKey("completed");
		root = profile.getKey("quests.completed");
		DataKey parent;
		for (CompletedQuest quest : this.completedQuests.values()) {
			parent = root.getRelative(quest.getName());
			parent.setInt("completed", quest.getTimesCompleted());
			parent.setLong("elapsed", quest.getElapsed());
			parent.setLong("finish", quest.getFinishTime());
			parent.setInt("quester", quest.getQuesterUID());
		}
		profile.save();
	}

	private void load() {
		DataKey root = profile.getKey("quests.current");
		questLoad: if (!root.getString("name").isEmpty()) {
			if (QuestManager.getQuest(root.getString("name")) == null) {
				Bukkit.getPlayer(name).sendMessage(
						ChatColor.GRAY
								+ "Previous in-progress quest "
								+ StringUtils.wrap(root.getString("name"),
										ChatColor.GRAY)
								+ " no longer exists and has been aborted.");
				root.removeKey("");
				break questLoad;
			}
			progress = new QuestProgress(root.getInt("giver"),
					Bukkit.getPlayer(name), root.getString("name"),
					root.getLong("start-time"));
			progress.setStep(root.getInt("step"));
			if (progress.getProgress() == null)
				break questLoad;
			int count = 0;
			DataKey parent;
			for (ObjectiveProgress questProgress : progress.getProgress()) {
				parent = root.getRelative(count + ".progress");
				if (questProgress == null || !parent.keyExists("")) {
					progress.getProgress()[count] = null;
					++count;
					continue;
				}
				questProgress.setAmountCompleted(parent.getInt("amount"));
				int itemID = parent.getInt("item.id");
				int amount = parent.getInt("item.amount");
				if (itemID != 0 && amount > 0) {
					ItemStack item = null;
					item = new ItemStack(itemID, amount);
					item.setDurability((short) parent.getInt("item.data"));
					questProgress.setLastItem(item);
				}
				if (parent.keyExists("location")) {
					questProgress.setLastLocation(LocationUtils.loadLocation(
							parent, true));
				}
				++count;
			}
		}
		if (!profile.getKey("quests").keyExists("completed")) {
			return;
		}
		root = profile.getKey("quests.completed");
		for (DataKey key : root.getSubKeys()) {
			addCompletedQuest(new CompletedQuest(key.name(),
					key.getInt("quester"), key.getInt("completed"),
					key.getLong("elapsed"), key.getLong("finish")));
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
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

	private static final Map<String, PlayerProfile> profiles = new HashMap<String, PlayerProfile>();

	public static Collection<PlayerProfile> getOnline() {
		return profiles.values();
	}

	public static PlayerProfile getProfile(String name, boolean register) {
		name = name.toLowerCase();
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

	public static boolean isOnline(String name) {
		return profiles.containsKey(name.toLowerCase());
	}

	public static void setProfile(String name, PlayerProfile profile) {
		name = name.toLowerCase();
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
}