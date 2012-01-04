package net.citizensnpcs.guards.flags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FlagList {
	private final Map<FlagType, Map<String, FlagInfo>> flags = getPopulatedMap();
	private final FlagSorter predicates = new FlagSorter(this);
	private LivingEntity result;

	public void addFlag(FlagType type, FlagInfo info) {
		if (info == null)
			throw new IllegalArgumentException("Info should not be null");
		if (type == FlagType.GROUP) {
			predicates.updateGroup(info);
		}
		getFlags(type).put(info.getName(), info);
	}

	public void addToAll(Set<Character> set, FlagInfo info) {
		List<FlagType> toAdd = Lists.newArrayList();
		if (set.size() == 1) {
			for (FlagType type : FlagType.values()) {
				toAdd.add(type);
			}
		} else {
			for (FlagType type : FlagType.values()) {
				if (type.isWithin(set)) {
					toAdd.add(type);
				}
			}
		}
		for (FlagType type : toAdd) {
			addFlag(type, info);
		}
	}

	public void clear() {
		for (FlagType type : FlagType.values()) {
			getFlags(type).clear();
		}
	}

	public boolean contains(FlagType type, String name) {
		return getFlags(type).get(name) != null;
	}

	public Map<String, FlagInfo> getFlags(FlagType type) {
		return flags.get(type);
	}

	private Map<FlagType, Map<String, FlagInfo>> getPopulatedMap() {
		Map<FlagType, Map<String, FlagInfo>> populated = Maps
				.newEnumMap(FlagType.class);
		for (FlagType type : FlagType.values()) {
			populated.put(type, new HashMap<String, FlagInfo>());
		}
		return populated;
	}

	public LivingEntity getResult() {
		return result;
	}

	public boolean process(Location base, List<LivingEntity> toProcess) {
		if (toProcess == null)
			return false;
		Iterable<LivingEntity> filtered = Iterables.filter(toProcess,
				predicates.getSorter());
		List<LivingEntity> possible = predicates.getPossible(filtered);
		switch (possible.size()) {
		case 0:
			return false;
		case 1:
			result = possible.get(0);
			return true;
		default:
			double lowest = Integer.MAX_VALUE;
			double distance;
			LivingEntity closest = null;
			for (LivingEntity entity : possible) {
				distance = base.distance(entity.getLocation());
				if (lowest > distance) {
					lowest = distance;
					closest = entity;
				}
			}
			result = closest;
			return true;
		}
	}

	public void processEntities(Location base, List<Entity> entities) {
		process(base, predicates.transformToLiving(entities));
	}

	public void removeFlag(FlagType type, String identifier) {
		getFlags(type).remove(identifier);
	}

	public enum FlagType {
		GROUP('g'),
		MOB('m'),
		PLAYER('p');
		private final char flag;

		FlagType(char flag) {
			this.flag = flag;
		}

		public char getCharacter() {
			return this.flag;
		}

		public boolean isWithin(Set<Character> flags) {
			return flags.contains(flag);
		}

		public static FlagType fromCharacter(char character) {
			for (FlagType type : FlagType.values()) {
				if (type.getCharacter() == character) {
					return type;
				}
			}
			return null;
		}

		public static FlagType parse(String passed) {
			try {
				return FlagType.valueOf(passed.toUpperCase());
			} catch (Exception ex) {
				return null;
			}
		}
	}
}