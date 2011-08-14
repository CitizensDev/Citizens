package net.citizensnpcs.questers.quests;

import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.rewards.EconpluginReward;
import net.citizensnpcs.questers.rewards.HealthReward;
import net.citizensnpcs.questers.rewards.ItemReward;
import net.citizensnpcs.questers.rewards.PermissionReward;
import net.citizensnpcs.questers.rewards.QuestReward;
import net.citizensnpcs.questers.rewards.RankReward;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class QuestFactory {
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
				// TODO: abstract this out per-class - perhaps a class that maps
				// rewards to 'RewardBuilder's?
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
						quest.addReward(new ItemReward(new ItemStack(id,
								amount, data), take));
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
					tempPath = questName + ".objectives." + step;
					for (Object objective : quests.getKeys(path + "." + step)) {
						path = tempPath + "." + objective;
						String type = quests.getString(path + ".type");
						if (type == null) {
							Messaging.log("Invalid quest objective (quest "
									+ (questCount + 1)
									+ ") - incorrect type specified.");
							continue;
						}
						Objective.Builder obj = new Objective.Builder(type);
						if (quests.pathExists(path + ".amount"))
							obj.amount(quests.getInt(path + ".amount"));
						if (quests.pathExists(path + ".npcdestination"))
							obj.destination(quests.getInt(path
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
							obj.item(stack);
						}
						if (quests.pathExists(path + ".location")) {
							obj.location(LocationUtils.loadLocation(quests,
									path, false));
						}
						if (quests.pathExists(path + ".message")) {
							obj.message(quests.getString(path + ".message"));
						}
						if (quests.pathExists(path + ".materialid")) {
							if (quests.getInt(path + ".materialid") != 0)
								obj.material(Material.getMaterial(quests
										.getInt(path + ".materialid")));
						}
						tempStep.add(obj.build());
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
			quests.setBoolean(path + ".take", reward.isTake());
			reward.save(quests, path);
			++count;
		}
		count = 0;
		path += ".objectives";
		int stepCount = 0;
		for (QuestStep step : quest.getObjectives().all()) {
			for (Objective objective : step.all()) {
				temp = path + "." + stepCount + "." + count;
				quests.setString(path + ".type", objective.getType());
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
					quests.setInt(path + ".item.data", item.getDurability());
				}
				if (objective.getLocation() != null) {
					LocationUtils.saveLocation(quests, objective.getLocation(),
							path, false);
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