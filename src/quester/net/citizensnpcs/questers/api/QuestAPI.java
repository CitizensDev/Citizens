package net.citizensnpcs.questers.api;

import java.util.Map;

import net.citizensnpcs.questers.quests.progress.QuestUpdater;
import net.citizensnpcs.questers.quests.types.BuildQuest;
import net.citizensnpcs.questers.quests.types.ChatQuest;
import net.citizensnpcs.questers.quests.types.CollectQuest;
import net.citizensnpcs.questers.quests.types.CombatQuest;
import net.citizensnpcs.questers.quests.types.DeliveryQuest;
import net.citizensnpcs.questers.quests.types.DestroyQuest;
import net.citizensnpcs.questers.quests.types.DistanceQuest;
import net.citizensnpcs.questers.quests.types.HuntQuest;
import net.citizensnpcs.questers.quests.types.KillNPCQuest;
import net.citizensnpcs.questers.quests.types.LocationQuest;
import net.citizensnpcs.questers.rewards.CommandReward.CommandRewardBuilder;
import net.citizensnpcs.questers.rewards.EconpluginReward.EconpluginRewardBuilder;
import net.citizensnpcs.questers.rewards.ExperienceReward.ExperienceRewardBuilder;
import net.citizensnpcs.questers.rewards.HealthReward.HealthRewardBuilder;
import net.citizensnpcs.questers.rewards.ItemReward.ItemRewardBuilder;
import net.citizensnpcs.questers.rewards.NPCReward.NPCRewardBuilder;
import net.citizensnpcs.questers.rewards.PermissionReward.PermissionRewardBuilder;
import net.citizensnpcs.questers.rewards.QuestReward.QuestRewardBuilder;
import net.citizensnpcs.questers.rewards.RankReward.RankRewardBuilder;
import net.citizensnpcs.questers.rewards.RewardBuilder;
import net.citizensnpcs.questers.rewards.TeleportReward.TeleportRewardBuilder;

import com.google.common.collect.Maps;

public class QuestAPI {
    private final static Map<String, QuestUpdater> questTypes = Maps.newHashMap();
    private final static Map<String, RewardBuilder> rewards = Maps.newHashMap();

    static {
        questTypes.put("build", new BuildQuest());
        questTypes.put("collect", new CollectQuest());
        questTypes.put("destroy block", new DestroyQuest());
        questTypes.put("delivery", new DeliveryQuest());
        questTypes.put("hunt", new HuntQuest());
        addQuestType(new KillNPCQuest(), "killnpc");
        addQuestType(new DistanceQuest(), "move distance", "distance");
        addQuestType(new LocationQuest(), "move location", "moveloc", "location", "loc");
        addQuestType(new CombatQuest(), "player combat", "combat");
        addQuestType(new ChatQuest(), "chat", "message");

        addRewardBuilder(new ExperienceRewardBuilder(), "xp", "experience");
        addRewardBuilder(new CommandRewardBuilder(), "command", "cmd");
        rewards.put("health", new HealthRewardBuilder());
        rewards.put("item", new ItemRewardBuilder());
        rewards.put("money", new EconpluginRewardBuilder());
        rewards.put("npc", new NPCRewardBuilder());
        addRewardBuilder(new PermissionRewardBuilder(), "permission", "perm");
        rewards.put("quest", new QuestRewardBuilder());
        addRewardBuilder(new RankRewardBuilder(), "rank", "group");
        addRewardBuilder(new TeleportRewardBuilder(), "teleport", "tp");
    }

    public static void addQuestType(QuestUpdater instance, String... identifiers) {
        for (String identifier : identifiers)
            questTypes.put(identifier.toLowerCase(), instance);
    }

    public static QuestUpdater getObjective(String identifier) {
        return questTypes.get(identifier.toLowerCase());
    }

    public static void addRewardBuilder(RewardBuilder instance, String... identifiers) {
        for (String identifier : identifiers)
            rewards.put(identifier.toLowerCase(), instance);
    }

    public static RewardBuilder getBuilder(String identifier) {
        return rewards.get(identifier.toLowerCase());
    }
}