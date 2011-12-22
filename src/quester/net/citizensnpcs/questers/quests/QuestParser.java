package net.citizensnpcs.questers.quests;

import java.util.List;
import java.util.Set;

import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.properties.DataSource;
import net.citizensnpcs.properties.RawObject;
import net.citizensnpcs.questers.QuestManager;
import net.citizensnpcs.questers.api.QuestAPI;
import net.citizensnpcs.questers.quests.Quest.QuestBuilder;
import net.citizensnpcs.questers.rewards.Requirement;
import net.citizensnpcs.questers.rewards.Reward;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class QuestParser {
	private static final Set<String> usedKeys = Sets.newHashSet("type",
			"amount", "item", "npcdestination", "rewards", "location",
			"string", "optional", "finishhere", "materialid", "message");

	public static void parse(DataSource quests) {
		questLoop: for (DataKey root : quests.getKey("").getSubKeys()) {
			String questName = root.name();
			QuestBuilder quest = new QuestBuilder(root.name());
			quest.description(root.getString("texts.description"));
			quest.granter(new RewardGranter(root.getString("texts.completion"),
					loadRewards(root.getRelative("rewards"))));
			quest.acceptanceText(root.getString("texts.acceptance"));
			quest.repeatLimit(root.getInt("repeats"));
			quest.requirements(Lists.transform(
					loadRewards(root.getRelative("requirements")), transformer));
			quest.delay(root.getLong("delay"));

			Objectives objectives = new Objectives();
			if (root.keyExists("objectives")) {
				for (DataKey key : root.getRelative("objectives").getSubKeys()) {
					if (!StringUtils.isNumber(key.name()))
						continue; // fix checking for objectives under rewards:
									// or messages:
					List<Objective> tempStep = Lists.newArrayList();
					for (DataKey objective : key.getSubKeys()) {
						if (!StringUtils.isNumber(objective.name()))
							continue;
						String type = objective.getString("type");
						if (type == null || type.isEmpty()
								|| QuestAPI.getObjective(type) == null) {
							Messaging
									.log("Invalid quest objective - incorrect type specified. Quest '"
											+ questName + "' not loaded.");
							continue questLoop;
						}
						Objective.Builder obj = new Objective.Builder(type);
						for (DataKey subKey : key.getSubKeys()) {
							if (usedKeys.contains(subKey.name()))
								continue;
							obj.param(subKey.name(),
									new RawObject(key.getRaw(subKey.name())));
						}
						if (key.keyExists("amount"))
							obj.amount(key.getInt("amount"));
						if (key.keyExists("npcdestination"))
							obj.destination(key.getInt("npcdestination"));
						if (key.keyExists("item")) {
							int id = key.getInt("item.id");
							int amount = key.getInt("item.amount");
							short data = 0;
							if (key.keyExists("item.data"))
								data = (short) key.getInt("item.data");
							obj.item(new ItemStack(id, amount, data));
						}
						if (key.keyExists("location")) {
							obj.location(LocationUtils.loadLocation(key, false));
						}
						obj.string(key.getString("string"));
						obj.optional(key.getBoolean("optional"));
						obj.completeHere(key.getBoolean("finishhere"));
						obj.granter(new RewardGranter(key.getString("message"),
								loadRewards(key.getRelative("rewards"))));

						if (key.keyExists("materialid")) {
							if (key.getInt("materialid") != 0)
								obj.material(Material.getMaterial(key
										.getInt("materialid")));
						}
						tempStep.add(obj.build());
					}
					RewardGranter granter = new RewardGranter(
							key.getString("message"),
							loadRewards(key.getRelative("rewards")));
					objectives.add(new QuestStep(tempStep, granter, key
							.getBoolean("finishhere")));
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

	public static void saveQuest(DataKey quests, Quest quest) {
		quests.setString("texts.description", quest.getDescription());
		quests.setString("texts.completion", quest.getGranter()
				.getCompletionMessage());
		quests.setString("texts.acceptance", quest.getAcceptanceText());
		quests.setInt("repeats", quest.getRepeatLimit());
		quests.removeKey("rewards"); // clear old rewards;
		DataKey temp = quests.getRelative("rewards");
		int i = 0;
		for (Reward reward : quest.getGranter().getRewards()) {
			DataKey root = temp.getRelative(Integer.toString(i));
			root.setBoolean("take", reward.isTake());
			reward.save(root);
			++i;
		}
		i = 0;
		for (Requirement reward : quest.getRequirements()) {
			DataKey root = temp.getRelative(Integer.toString(i));
			root.setBoolean("take", reward.isTake());
			reward.save(root);
			++i;
		}
		i = 0;
		temp = quests.getRelative("objectives");
		int stepCount = 0;
		for (QuestStep step : quest.getObjectives().steps()) {
			for (Objective objective : step.objectives()) {
				DataKey root = temp.getRelative(Integer.toString(stepCount))
						.getRelative(Integer.toString(i));
				root.setString("type", objective.getType());
				if (objective.getAmount() != -1)
					root.setInt("amount", objective.getAmount());
				if (objective.getDestNPCID() != -1)
					root.setInt("npcdestination", objective.getDestNPCID());
				if (!objective.getGranter().getCompletionMessage().isEmpty())
					root.setString("message", objective.getGranter()
							.getCompletionMessage());
				if (objective.getItem() != null) {
					ItemStack item = objective.getItem();
					root.setInt("item.id", item.getTypeId());
					root.setInt("item.amount", item.getAmount());
					root.setInt("item.data", item.getDurability());
				}
				if (objective.getLocation() != null) {
					LocationUtils.saveLocation(root, objective.getLocation(),
							false);
				}
				if (objective.getMaterial() != null) {
					root.setInt("materialid", objective.getMaterial().getId());
				}
				++i;
			}
			++stepCount;
		}
		// TODO: save rewards + requirements.
	}

	private static List<Reward> loadRewards(DataKey root) {
		List<Reward> rewards = Lists.newArrayList();
		for (DataKey key : root.getSubKeys()) {
			boolean take = key.getBoolean("take", false);
			String type = key.getString("type");
			Reward builder = QuestAPI.getBuilder(type) == null ? null
					: QuestAPI.getBuilder(type).build(key, take);
			if (builder != null) {
				rewards.add(builder);
			} else
				Messaging.log("Invalid type identifier " + type
						+ " for reward at " + key.name()
						+ ": reward not loaded.");
		}
		return rewards;
	}

	private static final Function<Reward, Requirement> transformer = new Function<Reward, Requirement>() {
		@Override
		public Requirement apply(Reward arg0) {
			return arg0 instanceof Requirement ? (Requirement) arg0 : null;
		}
	};
}