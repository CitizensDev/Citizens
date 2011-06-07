package com.fullwall.Citizens.NPCTypes.Questers.Quests;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.NPCTypes.Questers.Quest;
import com.fullwall.Citizens.NPCTypes.Questers.Objectives.Objective;
import com.fullwall.Citizens.NPCTypes.Questers.Objectives.Objectives;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.BuildQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.CollectQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.CombatQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.DeliveryQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.DestroyQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.DistanceQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.HuntQuest;
import com.fullwall.Citizens.NPCTypes.Questers.QuestTypes.LocationQuest;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager.QuestType;
import com.fullwall.Citizens.NPCTypes.Questers.Rewards.EconpluginReward;
import com.fullwall.Citizens.NPCTypes.Questers.Rewards.HealthReward;
import com.fullwall.Citizens.NPCTypes.Questers.Rewards.ItemReward;
import com.fullwall.Citizens.NPCTypes.Questers.Rewards.PermissionReward;
import com.fullwall.Citizens.NPCTypes.Questers.Rewards.QuestReward;
import com.fullwall.Citizens.Properties.ConfigurationHandler;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuestFactory {

	public static QuestIncrementer createIncrementer(HumanNPC npc,
			Player player, String questName, QuestType type) {
		switch (type) {
		case BUILD:
			return new BuildQuest(npc, player, questName);
		case COLLECT:
			return new CollectQuest(npc, player, questName);
		case DELIVERY:
			return new DeliveryQuest(npc, player, questName);
		case DESTROY_BLOCK:
			return new DestroyQuest(npc, player, questName);
		case HUNT:
			return new HuntQuest(npc, player, questName);
		case MOVE_DISTANCE:
			return new DistanceQuest(npc, player, questName);
		case MOVE_LOCATION:
			return new LocationQuest(npc, player, questName);
		case PLAYER_COMBAT:
			return new CombatQuest(npc, player, questName);
		default:
			return null;
		}
	}

	public static void instantiateQuests(ConfigurationHandler quests) {
		for (String questName : quests.getKeys(null)) {
			String path = questName + ".";
			Quest quest = new Quest(questName);
			String description = quests.getString(path + "texts.description");
			String completion = quests.getString(path + "texts.completion");
			quest.setDescription(description);
			quest.setCompletedText(completion);
			if (quests.pathExists(path + "rewards")) {
				for (String reward : quests.getKeys(path + "rewards")) {
					path += "rewards." + reward + ".";
					String type = quests.getString(path + "type");
					boolean take = quests.getBoolean(path + "take");
					if (type.equals("item")) {
						int id = quests.getInt(path + "id");
						int amount = quests.getInt(path + "amount");
						byte data = 0;
						if (quests.pathExists(path + "data"))
							data = (byte) quests.getInt(path + "data");
						ItemStack stack = new ItemStack(id, amount);
						stack.setData(new MaterialData(id, data));
						quest.addReward(new ItemReward(stack, take));
					} else if (type.equals("money")) {
						double amount = quests.getDouble(path + "amount");
						quest.addReward(new EconpluginReward(amount, take));
					} else if (type.equals("health")) {
						int amount = quests.getInt(path + "amount");
						quest.addReward(new HealthReward(amount, take));
					} else if (type.equals("permission")) {
						String permission = quests.getString(path
								+ "permission");
						quest.addReward(new PermissionReward(permission, take));
					} else if (type.equals("quest")) {
						String questToGive = quests.getString(path + "quest");
						quest.addReward(new QuestReward(questToGive));
					}
				}
			}
			path = questName + ".objectives";
			Objectives objectives = new Objectives();
			if (quests.pathExists(path)) {
				for (@SuppressWarnings("unused")
				String objective : quests.getKeys(path)) {
					QuestType type = QuestType.getType(quests.getString(
							path + ".type").toUpperCase());
					Objective obj = new Objective(type);
					if (quests.pathExists(path + ".amount"))
						obj.setAmount(quests.getInt(path + ".amount"));
					if (quests.pathExists(path + ".npcdestination"))
						obj.setDestination(quests.getInt(path
								+ ".npcdestination"));
					if (quests.pathExists(path + ".item")) {
						int id = quests.getInt(path + ".item.id");
						int amount = quests.getInt(path + ".item.amount");
						byte data = 0;
						if (quests.pathExists(path + ".item.data"))
							data = (byte) quests.getInt(path + ".item.data");
						ItemStack stack = new ItemStack(id, amount);
						stack.setData(new MaterialData(id, data));
						obj.setItem(stack);
					}
					if (quests.pathExists(path + ".location")) {
						String world = quests.getString(path
								+ ".location.world");
						double x = quests.getDouble(path + ".location.x");
						double y = quests.getDouble(path + ".location.y");
						double z = quests.getDouble(path + ".location.z");
						float yaw = (float) quests.getDouble(path
								+ ".location.yaw");
						float pitch = (float) quests.getDouble(path
								+ ".location.pitch");
						Location loc = new Location(Bukkit.getServer()
								.getWorld(world), x, y, z, yaw, pitch);
						obj.setLocation(loc);
					}
					if (quests.pathExists(path + ".message")) {
						obj.setMessage(quests.getString(path + ".message"));
					}
					objectives.add(obj);
				}
			}
		}
	}
}
