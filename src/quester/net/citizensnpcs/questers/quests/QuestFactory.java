package net.citizensnpcs.questers.quests;

import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.api.QuestAPI;
import net.citizensnpcs.questers.rewards.Reward;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class QuestFactory {

	public static void instantiateQuests(ConfigurationHandler quests) {
		questLoop: for (String questName : quests.getKeys(null)) {
			String path = questName;
			Quest quest = new Quest(questName);
			quest.setDescription(quests.getString(path + ".texts.description"));
			quest.setCompletedText(quests.getString(path + ".texts.completion"));
			quest.setAcceptanceText(quests
					.getString(path + ".texts.acceptance"));
			quest.setRepeatLimit(quests.getInt(path + ".repeats"));
			String tempPath = path;
			if (quests.pathExists(path + ".rewards")) {
				for (String reward : quests.getKeys(path + ".rewards")) {
					tempPath = path + ".rewards." + reward;
					Reward builder = loadReward(quests, tempPath);
					if (builder == null) {
						Messaging.log("Invalid reward type specified ("
								+ quests.getString(tempPath + ".type")
								+ "). Quest " + questName + " not loaded.");
						continue questLoop;
					}
					quest.addReward(builder);
				}
			}
			if (quests.pathExists(path + ".requirements")) {
				for (String requirement : quests
						.getKeys(path + ".requirements")) {
					tempPath = path + ".requirements." + requirement;
					Reward builder = loadReward(quests, tempPath);
					if (builder == null) {
						Messaging.log("Invalid requirement type specified ("
								+ quests.getString(tempPath + ".type")
								+ "). Quest " + questName + " not loaded.");
						continue questLoop;
					}
					quest.addRequirement(builder);
				}
			}
			Objectives objectives = new Objectives();
			path = tempPath = questName + ".objectives";
			if (quests.pathExists(path)) {
				for (Object step : quests.getKeys(path)) {
					QuestStep tempStep = new QuestStep();
					tempPath = questName + ".objectives." + step;
					for (Object objective : quests.getKeys(tempPath)) {
						path = tempPath + "." + objective;
						String type = quests.getString(path + ".type");
						if (type == null || type.isEmpty()
								|| QuestAPI.getObjective(type) == null) {
							Messaging
									.log("Invalid quest objective - incorrect type specified. Quest '"
											+ questName + "' not loaded.");
							continue questLoop;
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
							short data = 0;
							if (quests.pathExists(path + ".item.data"))
								data = (short) quests.getInt(path
										+ ".item.data");
							obj.item(new ItemStack(id, amount, data));
						}
						if (quests.pathExists(path + ".location")) {
							obj.location(LocationUtils.loadLocation(quests,
									path, false));
						}
						if (quests.pathExists(path + ".string")) {
							obj.string(quests.getString(path + ".string"));
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
			if (objectives.steps().size() == 0) {
				quest = null;
				Messaging.log("Quest " + questName
						+ " is invalid - no objectives set.");
				continue;
			}
			quest.setObjectives(objectives);
			QuestManager.addQuest(quest);
		}
	}

	public static void saveQuest(ConfigurationHandler quests, Quest quest) {
		String path = quest.getName();
		quests.setString(path + ".texts.description", quest.getDescription());
		quests.setString(path + ".texts.completion", quest.getCompletedText());
		quests.setString(path + ".texts.acceptance", quest.getCompletedText());
		quests.setInt(path + ".repeats", quest.getRepeatLimit());
		String temp = path + ".rewards";
		int count = 0;
		for (Reward reward : quest.getRewards()) {
			path = temp + "." + count;
			quests.setBoolean(path + ".take", reward.isTake());
			reward.save(quests, path);
			++count;
		}
		count = 0;
		for (Reward reward : quest.getRequirements()) {
			path = temp + "." + count;
			quests.setBoolean(path + ".take", reward.isTake());
			reward.save(quests, path);
			++count;
		}
		count = 0;
		path = quest.getName() + ".objectives";
		int stepCount = 0;
		for (QuestStep step : quest.getObjectives().steps()) {
			for (Objective objective : step.objectives()) {
				temp = path + "." + stepCount + "." + count;
				quests.setString(temp + ".type", objective.getType());
				if (objective.getAmount() != -1)
					quests.setInt(temp + ".amount", objective.getAmount());
				if (objective.getDestNPCID() != -1)
					quests.setInt(temp + ".npcdestination",
							objective.getDestNPCID());
				if (!objective.getMessage().isEmpty())
					quests.setString(temp + ".message", objective.getMessage());
				if (objective.getItem() != null) {
					ItemStack item = objective.getItem();
					quests.setInt(temp + ".item.id", item.getTypeId());
					quests.setInt(temp + ".item.amount", item.getAmount());
					quests.setInt(temp + ".item.data", item.getDurability());
				}
				if (objective.getLocation() != null) {
					LocationUtils.saveLocation(quests, objective.getLocation(),
							temp, false);
				}
				if (objective.getMaterial() != null) {
					quests.setInt(temp + ".materialid", objective.getMaterial()
							.getId());
				}
				++count;
			}
			++stepCount;
		}
		// TODO: save rewards + requirements.
	}

	public static Reward loadReward(Storage source, String root) {
		boolean take = source.getBoolean(root + ".take", false);
		String type = source.getString(root + ".type");
		return QuestAPI.getBuilder(type) == null ? null : QuestAPI.getBuilder(
				type).build(source, root, take);
	}
}