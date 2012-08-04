package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.data.PlayerProfile;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class QuestReward implements Requirement, Reward {
    private final String reward;
    private final boolean take;
    private final int times;

    QuestReward(String quest, int times, boolean take) {
        this.reward = quest;
        this.take = take;
        this.times = times;
    }

    @Override
    public boolean fulfilsRequirement(Player player) {
        if (times <= 0) {
            return !PlayerProfile.getProfile(player.getName()).hasCompleted(
                    reward);
        }
        return PlayerProfile.getProfile(player.getName()).getCompletedTimes(
                reward) >= times;
    }

    @Override
    public String getRequiredText(Player player) {
        return ChatColor.GRAY
                + (times > 0 ? "You must have completed the quest "
                        + StringUtils
                                .wrap(reward + " " + times, ChatColor.GRAY)
                        + StringUtils.pluralise("time", times) + "."
                        : "You've already completed the quest "
                                + StringUtils.wrap(reward) + ".");
    }

    @Override
    public void grant(Player player, int UID) {
        if (!take)
            new AssignQuestRunnable(player, UID, reward).schedule();
        else if (PlayerProfile.getProfile(player.getName()).getQuest()
                .equalsIgnoreCase(reward))
            PlayerProfile.getProfile(player.getName()).setProgress(null);
    }

    @Override
    public boolean isTake() {
        return take;
    }

    @Override
    public void save(Storage storage, String root) {
        storage.setString(root + ".quest", reward);
    }

    private static class AssignQuestRunnable implements Runnable {
        private final Player player;
        private final String quest;
        private final int UID;

        public AssignQuestRunnable(Player player, int UID, String quest) {
            this.player = player;
            this.UID = UID;
            this.quest = quest;
        }

        @Override
        public void run() {
            QuestManager.assignQuest(player, UID, quest);
        }

        public void schedule() {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Citizens.plugin,
                    this, 1);
        }
    }

    public static class QuestRewardBuilder implements RewardBuilder {
        @Override
        public Reward build(Storage storage, String root, boolean take) {
            return new QuestReward(storage.getString(root + ".quest"),
                    storage.getInt(root + ".times", 1), take);
        }
    }
}