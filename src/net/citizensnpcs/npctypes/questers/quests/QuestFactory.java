package net.citizensnpcs.npctypes.questers.quests;

import net.citizensnpcs.npctypes.questers.Quest;
import net.citizensnpcs.npctypes.questers.Reward;
import net.citizensnpcs.npctypes.questers.objectives.Objective;
import net.citizensnpcs.npctypes.questers.objectives.Objectives;
import net.citizensnpcs.npctypes.questers.objectives.QuestStep;
import net.citizensnpcs.npctypes.questers.objectives.Objectives.ObjectiveCycler;
import net.citizensnpcs.npctypes.questers.quests.QuestManager.QuestType;
import net.citizensnpcs.npctypes.questers.quests.QuestManager.RewardType;
import net.citizensnpcs.npctypes.questers.questtypes.BuildQuest;
import net.citizensnpcs.npctypes.questers.questtypes.CollectQuest;
import net.citizensnpcs.npctypes.questers.questtypes.CombatQuest;
import net.citizensnpcs.npctypes.questers.questtypes.DeliveryQuest;
import net.citizensnpcs.npctypes.questers.questtypes.DestroyQuest;
import net.citizensnpcs.npctypes.questers.questtypes.DistanceQuest;
import net.citizensnpcs.npctypes.questers.questtypes.HuntQuest;
import net.citizensnpcs.npctypes.questers.questtypes.LocationQuest;
import net.citizensnpcs.npctypes.questers.rewards.EconpluginReward;
import net.citizensnpcs.npctypes.questers.rewards.HealthReward;
import net.citizensnpcs.npctypes.questers.rewards.ItemReward;
import net.citizensnpcs.npctypes.questers.rewards.PermissionReward;
import net.citizensnpcs.npctypes.questers.rewards.QuestReward;
import net.citizensnpcs.npctypes.questers.rewards.RankReward;
import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class QuestFactory {

	public static QuestIncrementer createIncrementer(HumanNPC npc,
			Player player, String questName, QuestType type,
			ObjectiveCycler objectives) {
		switch (type) {
		case BUILD:
			return new BuildQuest(npc, player, questName, objectives);
		case COLLECT:
			return new CollectQuest(npc, player, questName, objectives);
		case DELIVERY:
			return new DeliveryQuest(npc, player, questName, objectives);
		case DESTROY_BLOCK:
			return new DestroyQuest(npc, player, questName, objectives);
		case HUNT:
			return new HuntQuest(npc, player, questName, objectives);
		case MOVE_DISTANCE:
			return new DistanceQuest(npc, player, questName, objectives);
		case MOVE_LOCATION:
			return new LocationQuest(npc, player, questName, objectives);
		case PLAYER_COMBAT:
			return new CombatQuest(npc, player, questName, objectives);
		default:
			return null;
		}
	}

	public static void instantiateQuests(ConfigurationHandler quests) {
		int questCount = 0;
		for (String questName : quests.getKeys(null)) {
			String path = questName + ".";
			Quest quest = new Quest(questName);
			quest.setDescription(quests.getString(path + "texts.description"));
			quest.setCompletedText(quests.getString(path + "texts.completion"));
			quest.setAcceptanceText(quests.getString(path + "texts.acceptance"));
			quest.setRepeatable(quests.getBoolean(path + ".repeatable"));
			String tempPath = path;
			if (quests.pathExists(path + "rewards")) {
				for (String reward : quests.getKeys(path + "rewards")) {
					path = tempPath + "rewards." + reward + ".";
					String type = quests.getString(path + "type");
					boolean take = quests.getBoolean(path + "take");
					if (type.equals("health")) {
						int amount = quests.getInt(path + "amount");
						quest.addReward(new HealthReward(amount, take));
					} else if (type.equals("item")) {
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
					} else if (type.equals("permission")) {
						String permission = quests.getString(path
								+ "permission");
						quest.addReward(new PermissionReward(permission, take));
					} else if (type.equals("quest")) {
						String questToGive = quests.getString(path + "quest");
						quest.addReward(new QuestReward(questToGive));
					} else if (type.equals("rank")) {
						String rank = quests.getString(path + "rank");
						quest.addReward(new RankReward(rank));
					}
				}
			}
			path = tempPath = questName + ".objectives";
			Objectives objectives = new Objectives();
			if (quests.pathExists(path)) {
				for (Object step : quests.getKeys(path)) {
					QuestStep tempStep = new QuestStep();
					for (Object objective : quests.getKeys(path + "." + step)) {
						path = tempPath + objective;
						QuestType type = QuestType.getType(quests
								.getString(path + ".type"));
						if (type == null) {
							Messaging.log("Invalid quest objective (quest "
									+ (questCount + 1)
									+ ") - incorrect type specified.");
							continue;
						}
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
								data = (byte) quests
										.getInt(path + ".item.data");
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
						if (quests.pathExists(path + ".materialid")) {
							if (quests.getInt(path + ".materialid") != 0)
								obj.setMaterial(Material.getMaterial(quests
										.getInt(path + ".materialid")));
						}
						tempStep.add(obj);
					}
					objectives.add(tempStep);
				}
			}
			if (objectives.all().size() == 0) {
				quest = null;
				Messaging.log("Quest number " + (questCount + 1)
						+ " is invalid - no objectives set.");
				continue;
			}
			quest.setObjectives(objectives);
			QuestManager.addQuest(quest);
			++questCount;
		}
		Messaging.log("Loaded " + questCount + " quests.");
	}

	public static void saveQuest(ConfigurationHandler quests, Quest quest) {
		String path = quest.getName();
		quests.setString(path + ".texts.description", quest.getDescription());
		quests.setString(path + ".texts.completion", quest.getCompletedText());
		quests.setString(path + ".texts.acceptance", quest.getCompletedText());
		quests.setBoolean(path + ".repeatable", quest.isRepeatable());
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
		int stepCount = 0;
		for (QuestStep step : quest.getObjectives().all()) {
			for (Objective objective : step.all()) {
				temp = path + "." + stepCount + "." + count;
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
					quests.setInt(path + ".item.data",
							item.getData() == null ? 0 : item.getData()
									.getData());
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
			++stepCount;
		}
	}
}