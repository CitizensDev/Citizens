package com.citizens.npctypes.guards;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

public class FlagsList {
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

	public LivingEntity getResult() {
		return result;
	}
}
