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

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

public class PlayerProfile {
    private final Map<String, CompletedQuest> completedQuests = Maps.newHashMap();

    private long lastSave;

    private final String name;

    private final ConfigurationHandler profile;

    private QuestProgress progress;

    private PlayerProfile(String name) {
        profile = new ConfigurationHandler("plugins/Citizens/profiles/" + name + ".yml");
        this.name = name;
        this.load();
    }

    public void addCompletedQuest(CompletedQuest quest) {
        completedQuests.put(quest.getName().toLowerCase(), quest);
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

    public Collection<CompletedQuest> getAllCompleted() {
        return Collections.unmodifiableCollection(completedQuests.values());
    }

    public CompletedQuest getCompletedQuest(String name) {
        return completedQuests.get(name.toLowerCase());
    }
    public int getCompletedTimes(String reward) {
        return hasCompleted(reward) ? getCompletedQuest(reward).getTimesCompleted() : 0;
    }
    public long getLastSaveTime() {
        return lastSave;
    }
    public QuestProgress getProgress() {
        return progress;
    }
    public String getQuest() {
        return progress == null ? "" : progress.getQuestName();
    }

    public boolean hasCompleted(String quest) {
        return completedQuests.containsKey(quest.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public boolean hasQuest() {
        return progress != null;
    }

    public boolean isOnline() {
        return Bukkit.getServer().getPlayer(name) != null;
    }

    private void load() {
        String path = "quests.current", temp = path;
        questLoad: if (!profile.getString(path + ".name").isEmpty()) {
            if (QuestManager.getQuest(profile.getString(path + ".name")) == null) {
                Bukkit.getServer()
                        .getPlayer(name)
                        .sendMessage(
                                ChatColor.GRAY + "Previous in-progress quest "
                                        + StringUtils.wrap(profile.getString(path + ".name"), ChatColor.GRAY)
                                        + " no longer exists and has been aborted.");
                profile.removeKey("quests.current");
                break questLoad;
            }
            progress = new QuestProgress(profile.getInt(path + ".giver"), Bukkit.getServer().getPlayer(name),
                    profile.getString(path + ".name"), profile.getLong(path + ".start-time"));
            progress.setStep(profile.getInt(path + ".step"));
            if (progress.getProgress() != null) {
                int count = 0;
                for (ObjectiveProgress questProgress : progress.getProgress()) {
                    temp = path + "." + count + ".progress";
                    if (questProgress == null || !profile.pathExists(temp)) {
                        progress.getProgress()[count] = null;
                        ++count;
                        continue;
                    }
                    questProgress.setAmountCompleted(this.profile.getInt(temp + ".amount"));
                    int itemID = profile.getInt(temp + ".item.id");
                    int amount = profile.getInt(temp + ".item.amount");
                    if (itemID != 0 && amount > 0) {
                        ItemStack item = null;
                        item = new ItemStack(itemID, amount);
                        item.setData(new MaterialData(itemID, (byte) profile.getInt(temp + ".item.data")));
                        questProgress.setLastItem(item);
                    }
                    if (profile.pathExists(temp + ".location")) {
                        questProgress.setLastLocation(LocationUtils.loadLocation(profile, temp, true));
                    }
                    ++count;
                }
            }
        }
        if (!profile.pathExists("quests.completed")) {
            return;
        }
        for (String key : profile.getKeys("quests.completed")) {
            path = "quests.completed." + key;
            addCompletedQuest(new CompletedQuest(key, profile.getInt(path + ".quester"), profile.getInt(path
                    + ".completed"), profile.getLong(path + ".elapsed"), profile.getLong(path + ".finish")));
        }
    }

    public void removeAllCompletedQuests() {
        completedQuests.clear();
    }

    public void removeCompletedQuest(String name) {
        completedQuests.remove(name.toLowerCase());
    }

    public void save() {
        this.lastSave = System.currentTimeMillis();
        if (profile.pathExists("quests.current"))
            profile.removeKey("quests.current");
        if (progress != null) {
            String path = "quests.current", oldPath = path;
            int count = 0;

            profile.setString(path + ".name", progress.getQuestName());
            profile.setInt(path + ".step", progress.getStep());
            profile.setLong(path + ".start-time", progress.getStartTime());
            profile.setInt(path + ".giver", progress.getQuesterUID());
            if (progress.getProgress() != null) {
                for (ObjectiveProgress current : progress.getProgress()) {
                    path = oldPath + "." + count + ".progress";
                    if (current == null) {
                        ++count;
                        continue;
                    }
                    profile.setInt(path + ".amount", current.getAmount());
                    if (current.getLastItem() != null) {
                        profile.setInt(path + ".item.id", current.getLastItem().getTypeId());
                        profile.setInt(path + ".item.amount", current.getLastItem().getAmount());
                        profile.setInt(path + ".item.data", current.getLastItem().getDurability());
                    }
                    if (current.getLastLocation() != null) {
                        LocationUtils.saveLocation(profile, current.getLastLocation(), path, true);
                    }
                    ++count;
                }
            }
        }
        if (profile.pathExists("quests.completed"))
            profile.removeKey("quests.completed");
        String path = "quests.completed.", temp;
        for (CompletedQuest quest : this.completedQuests.values()) {
            temp = path + quest.getName();
            profile.setInt(temp + ".completed", quest.getTimesCompleted());
            profile.setLong(temp + ".elapsed", quest.getElapsed());
            profile.setLong(temp + ".finish", quest.getFinishTime());
            profile.setInt(temp + ".quester", quest.getQuesterUID());
        }
        profile.save();
    }

    public void setProgress(QuestProgress progress) {
        this.progress = progress;
    }

    private static final Map<String, PlayerProfile> profiles = new HashMap<String, PlayerProfile>();

    public static Collection<PlayerProfile> getOnline() {
        return profiles.values();
    }

    public static PlayerProfile getProfile(String name) {
        return getProfile(name, true);
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

    public static boolean isOnline(String name) {
        return profiles.containsKey(name.toLowerCase());
    }

    public static void saveAll() {
        for (PlayerProfile profile : profiles.values()) {
            profile.save();
        }
    }

    public static void setProfile(String name, PlayerProfile profile) {
        name = name.toLowerCase();
        if (profile == null) {
            profiles.remove(name);
        } else {
            profiles.put(name, profile);
        }
    }
}