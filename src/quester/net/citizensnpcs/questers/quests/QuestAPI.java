package net.citizensnpcs.questers.quests;

import java.util.Map;

import net.citizensnpcs.questers.quests.types.BuildQuest;
import net.citizensnpcs.questers.quests.types.CollectQuest;
import net.citizensnpcs.questers.quests.types.CombatQuest;
import net.citizensnpcs.questers.quests.types.DestroyQuest;
import net.citizensnpcs.questers.quests.types.DistanceQuest;
import net.citizensnpcs.questers.quests.types.HuntQuest;
import net.citizensnpcs.questers.quests.types.LocationQuest;

import com.google.common.collect.Maps;

public class QuestAPI {
	private final static Map<String, QuestObjective> questTypes = Maps
			.newHashMap();
	static {
		questTypes.put("build", new BuildQuest());
		questTypes.put("collect", new CollectQuest());
		questTypes.put("destroy block", new DestroyQuest());
		questTypes.put("hunt", new HuntQuest());
		questTypes.put("move distance", new DistanceQuest());
		questTypes.put("move location", new LocationQuest());
		questTypes.put("player combat", new CombatQuest());
	}

	public static void addQuestType(String identifier, QuestObjective instance) {
		questTypes.put(identifier, instance);
	}

	public static QuestObjective getObjective(String identifier) {
		return questTypes.get(identifier.toLowerCase());
	}
}
