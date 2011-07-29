package com.citizens.npctypes.guards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.citizens.CreatureTask;
import com.citizens.Permission;
import com.citizens.utils.EntityUtils;
import com.citizens.utils.StringUtils;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.platymuus.bukkit.permissions.Group;

public class FlagSorter {
	private final FlagList list;
	private final Map<String, FlagInfo> groupMap = new HashMap<String, FlagInfo>();
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
			return list.groups.containsKey(group.getName())
					&& !list.groups.get(group.getName()).isSafe();
		}
	};

	private final Function<Group, String> groupToString = new Function<Group, String>() {
		@Override
		public String apply(Group group) {
			return group.getName();
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
				return isTargetable(list.mobs, StringUtils.format(CreatureTask
						.getCreature(entity).getType(), false));
			} else if (entity instanceof Player) {
				Player player = (Player) entity;
				if (isTargetable(list.players, player.getName())) {
					updateLowest(get(list.players, player.getName()));
					return true;
				} else {
					if (groupMap.get(player.getName()) != null) {
						return true;
					}
					List<String> transformed = ImmutableList.copyOf(Iterables
							.transform(Iterables.filter(
									Permission.getGroups(player), groupSorter),
									groupToString));
					TreeSet<FlagInfo> sorted = getSortedFlags(list.groups,
							transformed);
					if (sorted.size() > 0) {
						FlagInfo info = sorted.first();
						groupMap.put(player.getName(), info);
						updateLowest(info);
					}
					return sorted.size() == 0;
				}
			} else {
				if (isTargetable(list.mobs, EntityUtils.getMonsterName(entity))) {
					updateLowest(get(list.mobs,
							EntityUtils.getMonsterName(entity)));
					return true;
				}
				return false;
			}
		}

		private FlagInfo get(Map<String, FlagInfo> source, String name) {
			return source.get(name);
		}
	};

	private void updateLowest(FlagInfo info) {
		if (info.priority() < lowestFound) {
			lowestFound = info.priority();
		}
	}

	TreeSet<FlagInfo> getSortedFlags(Map<String, FlagInfo> process) {
		return getSortedFlags(process.values());
	}

	TreeSet<FlagInfo> getSortedFlags(Collection<FlagInfo> process) {
		TreeSet<FlagInfo> sorted = new TreeSet<FlagInfo>(priorityComparer);
		sorted.addAll(process);
		return sorted;
	}

	TreeSet<FlagInfo> getSortedFlags(Map<String, FlagInfo> source,
			Collection<String> toProcess) {
		List<FlagInfo> processed = new ArrayList<FlagInfo>();
		for (String string : toProcess) {
			if (source.get(string) == null)
				throw new IllegalArgumentException(
						"source didn't contain a corresponding flag");
			processed.add(source.get(string));
		}
		return getSortedFlags(processed);
	}

	static boolean isTargetable(Map<String, FlagInfo> search, String key) {
		return search.containsKey(key) && !search.get(key).isSafe();
	}

	Predicate<LivingEntity> getSorter() {
		return entitySorter;
	}

	Comparator<FlagInfo> getPriorityCompare() {
		return priorityComparer;
	}

	List<LivingEntity> getPossible(Iterable<LivingEntity> toProcess) {
		List<LivingEntity> processed = Lists.newArrayList();
		FlagInfo retrieved = null;
		for (LivingEntity entity : toProcess) {
			if (CreatureTask.getCreature(entity) != null) {
				retrieved = list.mobs.get(StringUtils.format(CreatureTask
						.getCreature(entity).getType(), false));
			} else if (entity instanceof Player) {
				Player player = (Player) entity;
				retrieved = list.players.get(player.getName()) == null ? groupMap
						.get(player.getName()) : list.players.get(player
						.getName());
			} else {
				retrieved = list.mobs.get(EntityUtils.getMonsterName(entity));
			}
			if (retrieved.priority() == lowestFound)
				processed.add(entity);
		}
		reset();
		return processed;
	}

	private void reset() {
		lowestFound = 21;
	}

	public List<LivingEntity> transformToLiving(List<Entity> entities) {
		return ImmutableList.copyOf(Iterables.transform(
				Iterables.filter(entities, livingFilterer), livingTransformer));
	}
}
