package com.fullwall.Citizens.Properties.Properties;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.Reward;
import com.fullwall.Citizens.NPCTypes.Questers.Objectives.Objective;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestFactory;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager.RewardType;
import com.fullwall.Citizens.Properties.ConfigurationHandler;

public class QuestProperties {
	private static ConfigurationHandler quests;

	public static void initialize() {
		quests = new ConfigurationHandler("plugins/Citizens/quests.yml");
		QuestFactory.instantiateQuests(quests);
	}

	public static void save() {
		quests.save();
	}

	public static void saveQuest(Quest quest) {
		String path = quest.getName();
		quests.setString(path + ".texts.description", quest.getDescription());
		quests.setString(path + ".texts.completion", quest.getCompletedText());
		String temp = path + ".rewards.";
		int count = 0;
		for (Reward reward : quest.getRewards()) {
			path = temp + count;
			if (reward.getType() != RewardType.QUEST
					&& reward.getType() != RewardType.RANK)
				quests.setBoolean(path + ".take", reward.isTake());
			switch (reward.getType()) {
			case HEALTH:
				quests.setInt(path + ".amount", (Integer) reward.getReward());
				break;
			case ITEM:
				ItemStack item = (ItemStack) reward.getReward();
				quests.setInt(path + ".id", item.getTypeId());
				quests.setInt(path + ".amount", item.getAmount());
				quests.setInt(path + ".data", item.getData() == null ? 0 : item
						.getData().getData());
				break;
			case MONEY:
				quests.setDouble(path + ".amount", (Double) reward.getReward());
				break;
			case PERMISSION:
				quests.setString(path + ".permission",
						(String) reward.getReward());
				break;
			case QUEST:
				quests.setString(path + ".quest", (String) reward.getReward());
				break;
			case RANK:
				quests.setString(path + ".rank", (String) reward.getReward());
				break;
			}
			++count;
		}
		count = 0;
		path += ".objectives";
		for (Objective objective : quest.getObjectives().all()) {
			temp = path + count;
			quests.setString(path + ".type", objective.getType().name()
					.toLowerCase());
			if (objective.getAmount() != -1)
				quests.setInt(path + ".amount", objective.getAmount());
			if (objective.getDestinationNPCID() != -1)
				quests.setInt(path + ".npcdestination",
						objective.getDestinationNPCID());
			if (!objective.getMessage().isEmpty())
				quests.setString(path + ".message", objective.getMessage());
			if (objective.getItem() != null) {
				ItemStack item = objective.getItem();
				quests.setInt(path + ".item.id", item.getTypeId());
				quests.setInt(path + ".item.amount", item.getAmount());
				quests.setInt(path + ".item.data", item.getData() == null ? 0
						: item.getData().getData());
			}
			if (objective.getLocation() != null) {
				Location loc = objective.getLocation();
				quests.setString(path + ".location.world", loc.getWorld()
						.getName());
				quests.setDouble(path + ".location.x", loc.getX());
				quests.setDouble(path + ".location.y", loc.getY());
				quests.setDouble(path + ".location.z", loc.getZ());
				quests.setDouble(path + ".location.yaw", loc.getYaw());
				quests.setDouble(path + ".location.pitch", loc.getPitch());
			}
			if (objective.getMaterial() != null) {
				quests.setInt(path + ".materialid", objective.getMaterial()
						.getId());
			}
			++count;
		}
	}
}
