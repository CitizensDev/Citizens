package com.citizens.npctypes.guards;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

public class FlagList {
	final Map<String, FlagInfo> players = Maps.newHashMap();
	final Map<String, FlagInfo> groups = Maps.newHashMap();
	final Map<String, FlagInfo> mobs = Maps.newHashMap();
	final FlagSorter predicates = new FlagSorter(this);
	private LivingEntity result;

	public boolean process(Location base, List<LivingEntity> toProcess) {
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
			double lowest = Integer.MAX_VALUE,
			distance;
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

	public LivingEntity getResult() {
		return result;
	}

	public void addFlag(FlagType type, FlagInfo info) {
		getByType(type).put(info.getName(), info);
	}

	public void removeFlag(FlagType type, String identifier) {
		getByType(type).remove(identifier);
	}

	public Map<String, FlagInfo> getByType(FlagType type) {
		switch (type) {
		case PLAYER:
			return players;
		case GROUP:
			return groups;
		case MOB:
			return mobs;
		}
		return null;
	}

	public enum FlagType {
		GROUP,
		MOB,
		PLAYER;
	}
}
