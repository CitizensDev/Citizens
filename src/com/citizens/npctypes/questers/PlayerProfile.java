package com.citizens.npctypes.questers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.citizens.interfaces.Storage;
import com.citizens.npcs.NPCManager;
import com.citizens.npctypes.questers.objectives.Objective.Progress;
import com.citizens.npctypes.questers.quests.QuestIncrementer;
import com.citizens.npctypes.questers.quests.QuestManager;
import com.citizens.npctypes.questers.quests.QuestProgress;
import com.citizens.properties.ConfigurationHandler;

public class PlayerProfile {
	private final ConfigurationHandler profile;
	private int rank;
	private final List<CompletedQuest> completedQuests = new ArrayList<CompletedQuest>();
	private QuestProgress progress;
	private final String name;

	public PlayerProfile(String name) {
		profile = new ConfigurationHandler("plugins/Citizens/Player Profiles/"
				+ name + ".yml");
		this.name = name;
		this.load();
		rank = 1;
	}

	public List<CompletedQuest> getCompletedQuests() {
		return completedQuests;
	}

	public void addCompletedQuest(CompletedQuest quest) {
		completedQuests.add(quest);
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

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}

	public boolean hasQuest() {
		return progress != null;
	}

	public Storage getStorage() {
		return profile;
	}

	public void save() {
		if (progress != null) {
			String path = "quests.current.";
			String oldPath = path;
			int count = 0;
			for (QuestIncrementer incrementer : progress.getIncrementers()) {
				path = oldPath + count;
				profile.setString(path + "name", progress.getQuestName());
				profile.setInt(path + "step", progress.getStep());
				profile.setLong(path + "start-time", progress.getStartTime());
				profile.setInt(path + "giver", progress.getQuesterUID());
				Progress questProgress = incrementer.getProgress();
				profile.setInt(path + "progress.amount",
						questProgress.getAmount());
				if (questProgress.getLastItem() != null) {
					profile.setInt(path + "progress.item.id", questProgress
							.getLastItem().getTypeId());
					profile.setInt(path + "progress.item.amount", questProgress
							.getLastItem().getAmount());
					profile.setInt(path + "progress.item.id", questProgress
							.getLastItem().getData() == null ? 0
							: questProgress.getLastItem().getData().getData());
				}
				if (questProgress.getLastLocation() != null) {
					profile.setString(path + "progress.location.world",
							questProgress.getLastLocation().getWorld()
									.getName());
					profile.setDouble(path + "progress.location.x",
							questProgress.getLastLocation().getX());
					profile.setDouble(path + "progress.location.y",
							questProgress.getLastLocation().getY());
					profile.setDouble(path + "progress.location.z",
							questProgress.getLastLocation().getZ());
					profile.setDouble(path + "progress.location.yaw",
							questProgress.getLastLocation().getYaw());
					profile.setDouble(path + "progress.location.pitch",
							questProgress.getLastLocation().getPitch());
				}
				++count;
			}
		}
		profile.save();
	}

	private void load() {
		String path = "quests.current.";
		if (!profile.getString(path + "name").isEmpty()) {
			QuestManager.assignQuest(NPCManager.get(profile.getInt(path
					+ "giver")), Bukkit.getServer().getPlayer(name), profile
					.getString(path + "name"));
			progress.setStartTime(profile.getInt(path + "start-time"));
			progress.setStep(profile.getInt(path + "step"));
			for (int count = 0; count <= progress.getIncrementers().size(); ++count) {
				Progress questProgress = progress.getIncrementer(count)
						.getProgress();
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
				String name = profile.getString(path
						+ "progress.location.world");
				if (!name.isEmpty()) {
					Location loc = null;
					double x = profile.getDouble(path + "progress.location.x");
					double y = profile.getDouble(path + "progress.location.y");
					double z = profile.getDouble(path + "progress.location.z");
					float yaw = (float) profile.getDouble(path
							+ "progress.location.yaw");
					float pitch = (float) profile.getDouble(path
							+ "progress.location.pitch");
					loc = new Location(Bukkit.getServer().getWorld(name), x, y,
							z, yaw, pitch);
					questProgress.setLastLocation(loc);
				}
			}
		}
	}
}