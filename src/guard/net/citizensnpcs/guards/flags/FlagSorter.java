package net.citizensnpcs.guards.flags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.guards.Guard;
import net.citizensnpcs.guards.flags.FlagList.FlagType;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;
import net.citizensnpcs.utils.EntityUtils;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeMultiset;
import com.platymuus.bukkit.permissions.Group;

public class FlagSorter {
	// TODO: perhaps we should make a sorted copy as well of the base flags.
	// Needs some cleaning up of code... perhaps less verbosity can be achieved.
	// Optimisations are definitely possible.
	private final FlagList list;
	private final Map<String, FlagInfo> groupMap = new HashMap<String, FlagInfo>();
	private final FlagType GROUPS = FlagType.GROUP, PLAYERS = FlagType.PLAYER,
			MOBS = FlagType.MOB;
	private int lowestFound = 21;

	public FlagSorter(FlagList list) {
		this.list = list;
	}

	private static final Function<Entity, LivingEntity> livingTransformer = new Function<Entity, LivingEntity>() {
		@Override
		public LivingEntity apply(Entity entity) {
			return (LivingEntity) entity;
		}
	};
	private static final Predicate<Entity> livingFilterer = new Predicate<Entity>() {
		@Override
		public boolean apply(Entity entity) {
			return entity instanceof LivingEntity
					&& !((LivingEntity) entity).isDead();
		}

	};
	private final Predicate<Group> groupSorter = new Predicate<Group>() {
		@Override
		public boolean apply(Group group) {
			String name = group.getName().toLowerCase();
			if (!getByType(GROUPS).containsKey(name))
				name = "all";
			return getByType(GROUPS).containsKey(name)
					&& !getByType(GROUPS).get(name).isSafe();
		}
	};

	private final Function<Group, String> groupToString = new Function<Group, String>() {
		@Override
		public String apply(Group group) {
			return group.getName().toLowerCase();
		}
	};

	private final Comparator<FlagInfo> priorityComparer = new Comparator<FlagInfo>() {
		@Override
		public int compare(FlagInfo first, FlagInfo other) {
			int priority = first.priority(), second = other.priority();
			return second < priority ? 1 : second > priority ? -1 : 0;
		}
	};

	private final Predicate<LivingEntity> entitySorter = new Predicate<LivingEntity>() {
		@Override
		public boolean apply(LivingEntity entity) {
			if (CreatureTask.getCreature(entity) != null) {
				String search = StringUtils.format(
						CreatureTask.getCreature(entity).getType(), false);
				if (isTargetable(getByType(MOBS), search)) {
					updateLowest(get(getByType(MOBS), search));
					return true;
				}
				return false;
			} else if (entity instanceof Player) {
				Player player = (Player) entity;
				String name = player.getName().toLowerCase();
				if (isTargetable(getByType(PLAYERS), name)) {
					updateLowest(get(getByType(PLAYERS), name));
					return true;
				} else {
					if (!PermissionManager.superPermsEnabled()) {
						return false;
					}
					if (groupMap.get(name) != null) {
						return true;
					}
					if (PermissionManager.getGroups(player) == null) {
						return false;
					}
					List<String> transformed = Lists.newArrayList((Iterables
							.transform(Iterables.filter(
									PermissionManager.getGroups(player),
									groupSorter), groupToString)));
					TreeMultiset<FlagInfo> sorted = getSortedFlags(
							getByType(GROUPS), transformed);
					if (sorted.size() > 0) {
						FlagInfo info = sorted.elementSet().first();
						groupMap.put(name, info);
						updateLowest(info);
					}
					return sorted.size() == 0;
				}
			} else {
				if (isTargetable(getByType(MOBS),
						EntityUtils.getMonsterName(entity))) {
					updateLowest(get(getByType(MOBS),
							EntityUtils.getMonsterName(entity)));
					return true;
				}
				return false;
			}
		}

	};

	private FlagInfo get(Map<String, FlagInfo> source, String name) {
		if (!source.containsKey(name))
			name = "all";
		return source.get(name);
	}

	List<LivingEntity> getPossible(HumanNPC npc,
			Iterable<LivingEntity> toProcess) {
		List<LivingEntity> processed = Lists.newArrayList();
		FlagInfo retrieved;
		Guard guard = npc.getType("guard");
		for (LivingEntity entity : toProcess) {
			retrieved = null;
			if (NPCManager.isNPC(entity)) {
			} else if (CreatureTask.getCreature(entity) != null) {
				retrieved = get(getByType(MOBS), StringUtils.format(
						CreatureTask.getCreature(entity).getType(), false));
			} else if (entity instanceof Player) {
				String name = ((Player) entity).getName().toLowerCase();
				if (!name.equals(npc.getOwner().toLowerCase())) {
					retrieved = get(getByType(PLAYERS), name) == null ? groupMap
							.get(name) : get(getByType(PLAYERS), name);
				}
			} else {
				retrieved = get(getByType(MOBS),
						EntityUtils.getMonsterName(entity));
			}
			if (retrieved != null
					&& LocationUtils.withinRange(npc.getBaseLocation(),
							entity.getLocation(), guard.getProtectionRadius())
					&& retrieved.priority() == lowestFound) {
				processed.add(entity);
			}
		}
		reset();
		return processed;
	}

	private void updateLowest(FlagInfo info) {
		if (info.priority() < lowestFound) {
			lowestFound = info.priority();
		}
	}

	private final Map<String, FlagInfo> getByType(FlagType type) {
		return list.getFlags(type);
	}

	TreeMultiset<FlagInfo> getSortedFlags(Map<String, FlagInfo> process) {
		return getSortedFlags(process.values());
	}

	TreeMultiset<FlagInfo> getSortedFlags(Collection<FlagInfo> process) {
		TreeMultiset<FlagInfo> sorted = TreeMultiset.create(priorityComparer);
		sorted.addAll(process);
		return sorted;
	}

	TreeMultiset<FlagInfo> getSortedFlags(Map<String, FlagInfo> source,
			Collection<String> toProcess) {
		List<FlagInfo> processed = new ArrayList<FlagInfo>();
		for (String string : toProcess) {
			if (source.get(string) == null) {
				throw new IllegalArgumentException(
						"source didn't contain a corresponding flag");
			}
			processed.add(source.get(string));
		}
		return getSortedFlags(processed);
	}

	static boolean isTargetable(Map<String, FlagInfo> search, String key) {
		if (!search.containsKey(key)) {
			key = "all";
		}
		return search.containsKey(key) && !search.get(key).isSafe();
	}

	private void reset() {
		lowestFound = 21;
	}

	public List<LivingEntity> transformToLiving(List<Entity> entities) {
		return Lists.newArrayList(Iterables.transform(
				Iterables.filter(entities, livingFilterer), livingTransformer));
	}

	Predicate<LivingEntity> getSorter() {
		return entitySorter;
	}

	Comparator<FlagInfo> getPriorityCompare() {
		return priorityComparer;
	}

	public void updateGroup(FlagInfo info) {
		Group group = PermissionManager.getGroup(info.getName());
		if (group == null)
			return;
		for (String name : group.getPlayers()) {
			if (groupMap.containsKey(name))
				groupMap.put(name, info);
		}
	}
}