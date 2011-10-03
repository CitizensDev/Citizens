package net.citizensnpcs.questers.quests;

import java.util.List;

import net.citizensnpcs.properties.ConfigurationHandler;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.api.QuestAPI;
import net.citizensnpcs.questers.quests.Quest.QuestBuilder;
import net.citizensnpcs.questers.rewards.Reward;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

public class QuestFactory {

	public static void instantiateQuests(ConfigurationHandler quests) {
		questLoop: for (Object questName : quests.getKeys(null)) {
			String path = questName.toString();
			QuestBuilder quest = new QuestBuilder(questName.toString());
			quest.description(quests.getString(path + ".texts.description"));
			quest.granter(new RewardGranter(quests.getString(path
					+ ".texts.completion"), loadRewards(quests, path
					+ ".rewards")));
			quest.acceptanceText(quests.getString(path + ".texts.acceptance"));
			quest.repeatLimit(quests.getInt(path + ".repeats"));
			quest.requirements(loadRewards(quests, path + ".requirements"));
			String tempPath = path;

			Objectives objectives = new Objectives();
			path = tempPath = questName + ".objectives";
			if (quests.pathExists(path)) {
				for (Object step : quests.getKeys(path)) {
					tempPath = questName + ".objectives." + step;
					List<Objective> tempStep = Lists.newArrayList();
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
						obj.string(quests.getString(path + ".string"));
						obj.optional(quests.getBoolean(path + ".optional"));
						obj.completeHere(quests
								.getBoolean(path + ".finishhere"));
						obj.granter(new RewardGranter(quests.getString(path
								+ ".message"), loadRewards(quests, path
								+ ".rewards")));

						if (quests.pathExists(path + ".materialid")) {
							if (quests.getInt(path + ".materialid") != 0)
								obj.material(Material.getMaterial(quests
										.getInt(path + ".materialid")));
						}
						tempStep.add(obj.build());
					}
					RewardGranter granter = new RewardGranter(
							quests.getString(tempPath + ".message"),
							loadRewards(quests, tempPath + ".rewards"));
					objectives.add(new QuestStep(tempStep, granter));
				}
			}
			if (objectives.steps().size() == 0) {
				quest = null;
				Messaging.log("Quest " + questName
						+ " is invalid - no objectives set.");
				continue;
			}
			quest.objectives(objectives);
			QuestManager.addQuest(quest.create());
		}
	}

	public static void saveQuest(ConfigurationHandler quests, Quest quest) {
		String path = quest.getName();
		quests.setString(path + ".texts.description", quest.getDescription());
		quests.setString(path + ".texts.completion", quest.getGranter()
				.getCompletionMessage());
		quests.setString(path + ".texts.acceptance", quest.getAcceptanceText());
		quests.setInt(path + ".repeats", quest.getRepeatLimit());
		String temp = path + ".rewards";
		int count = 0;
		for (Reward reward : quest.getGranter().getRewards()) {
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
				if (!objective.getGranter().getCompletionMessage().isEmpty())
					quests.setString(temp + ".message", objective.getGranter()
							.getCompletionMessage());
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

	private static List<Reward> loadRewards(ConfigurationHandler source,
			String root) {
		List<Reward> rewards = Lists.newArrayList();
		String path;
		List<String> keys = source.getKeys(root);
		if (keys == null)
			return Lists.newArrayList();
		for (String key : keys) {
			path = root + "." + key;
			boolean take = source.getBoolean(path + ".take", false);
			String type = source.getString(path + ".type");
			Reward builder = QuestAPI.getBuilder(type) == null ? null
					: QuestAPI.getBuilder(type).build(source, path, take);
			if (builder != null) {
				rewards.add(builder);
			} else
				Messaging.log("Invalid type identifier " + type
						+ " for reward at " + path + ": reward not loaded.");
		}
		return rewards;
	}
}