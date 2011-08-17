package net.citizensnpcs.questers;

import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.npcs.NPCManager;
import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.quests.CompletedQuest;
import net.citizensnpcs.questers.quests.ObjectiveProgress;
import net.citizensnpcs.questers.quests.QuestProgress;
import net.citizensnpcs.utils.LocationUtils;

import org.bukkit.Bukkit;
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

	public static PlayerProfile getProfile(String name) {
		if (profiles.get(name) == null)
			profiles.put(name, new PlayerProfile(name));
		return profiles.get(name);
	}

	public static void setProfile(String name, PlayerProfile profile) {
		profiles.put(name, profile);
	}

	public PlayerProfile(String name) {
		profile = new ConfigurationHandler("plugins/Citizens/Player Profiles/"
				+ name + ".yml");
		this.name = name;
		this.load();
	}

	public void addCompletedQuest(CompletedQuest quest) {
		completedQuests.put(quest.getName(), quest);
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

	public boolean hasCompleted(String reward) {
		return completedQuests.containsKey(reward);
	}

	public Storage getStorage() {
		return profile;
	}

	public void save() {
		if (progress != null) {
			String path = "quests.current.", oldPath = path;
			int count = 0;

			profile.setString(path + "name", progress.getQuestName());
			profile.setInt(path + "step", progress.getStep());
			profile.setLong(path + "start-time", progress.getStartTime());
			profile.setInt(path + "giver", progress.getQuesterUID());

			for (ObjectiveProgress current : progress.getProgress()) {
				path = oldPath + count + ".progress";
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
					LocationUtils
							.saveLocation(profile, current.getLastLocation(),
									path + ".location", true);
				}
				++count;
			}
		}
		profile.save();
	}

	private void load() {
		String path = "quests.current.";
		if (!profile.getString(path + "name").isEmpty()) {
			progress = new QuestProgress(NPCManager.get(profile.getInt(path
					+ "giver")), Bukkit.getServer().getPlayer(name),
					profile.getString(path + "name"));
			progress.setStartTime(profile.getInt(path + "start-time"));
			progress.setStep(profile.getInt(path + "step"));

			for (ObjectiveProgress questProgress : progress.getProgress()) {
				questProgress.setAmountCompleted(this.profile.getInt(path
						+ "progress.amount"));
				int itemID = profile.getInt(path + "progress.item.id");
				int amount = profile.getInt(path + "progress.item.amount");
				if (itemID != 0 && amount > 0) {
					ItemStack item = null;
					item = new ItemStack(itemID, amount);
					item.setData(new MaterialData(itemID, (byte) profile
							.getInt(path + "progress.item.data")));
					questProgress.setLastItem(item);
				}
				if (profile.pathExists("progress.location")) {
					questProgress.setLastLocation(LocationUtils.loadLocation(
							profile, path + "progress.location", true));
				}
			}
		}
	}
}