package net.citizensnpcs.questers.quests;

import java.util.Map;

import net.citizensnpcs.questers.RewardBuilder;
import net.citizensnpcs.questers.quests.types.BuildQuest;
import net.citizensnpcs.questers.quests.types.CollectQuest;
import net.citizensnpcs.questers.quests.types.CombatQuest;
import net.citizensnpcs.questers.quests.types.DeliveryQuest;
import net.citizensnpcs.questers.quests.types.DestroyQuest;
import net.citizensnpcs.questers.quests.types.DistanceQuest;
import net.citizensnpcs.questers.quests.types.HuntQuest;
import net.citizensnpcs.questers.quests.types.LocationQuest;
import net.citizensnpcs.questers.rewards.EconpluginReward.EconpluginRewardBuilder;
import net.citizensnpcs.questers.rewards.HealthReward.HealthRewardBuilder;
import net.citizensnpcs.questers.rewards.ItemReward.ItemRewardBuilder;
import net.citizensnpcs.questers.rewards.PermissionReward.PermissionRewardBuilder;
import net.citizensnpcs.questers.rewards.QuestReward.QuestRewardBuilder;
import net.citizensnpcs.questers.rewards.RankReward.RankRewardBuilder;

import com.google.common.collect.Maps;

public class QuestAPI {
	private final static Map<String, QuestObjective> questTypes = Maps
			.newHashMap();
	private final static Map<String, RewardBuilder> rewards = Maps.newHashMap();
	static {
		questTypes.put("build", new BuildQuest());
		questTypes.put("collect", new CollectQuest());
		questTypes.put("destroy block", new DestroyQuest());
		questTypes.put("delivery", new DeliveryQuest());
		questTypes.put("hunt", new HuntQuest());
		questTypes.put("move distance", new DistanceQuest());
		questTypes.put("move location", new LocationQuest());
		questTypes.put("player combat", new CombatQuest());

		rewards.put("health", new HealthRewardBuilder());
		rewards.put("item", new ItemRewardBuilder());
		rewards.put("money", new EconpluginRewardBuilder());
		rewards.put("permission", new PermissionRewardBuilder());
		rewards.put("quest", new QuestRewardBuilder());
		rewards.put("rank", new RankRewardBuilder());
	}

	public static void addQuestType(String identifier, QuestObjective instance) {
		questTypes.put(identifier, instance);
	}

	public static QuestObjective getObjective(String identifier) {
		return questTypes.get(identifier.toLowerCase());
	}

	public static void addRewardBuilder(String identifier,
			RewardBuilder instance) {
		rewards.put(identifier, instance);
	}

	public static RewardBuilder getBuilder(String identifier) {
		return rewards.get(identifier.toLowerCase());
	}
}