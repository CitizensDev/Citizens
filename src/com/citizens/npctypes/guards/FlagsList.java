package com.citizens.npctypes.guards;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.citizens.CreatureTask;
import com.citizens.Permission;
import com.citizens.utils.EntityUtils;
import com.citizens.utils.StringUtils;
import com.google.common.base.Predicate;
import com.platymuus.bukkit.permissions.Group;

public class FlagsList {
	private final TreeSet<FlagInfo> players = new TreeSet<FlagInfo>();
	private final TreeSet<FlagInfo> groups = new TreeSet<FlagInfo>();
	private final TreeSet<FlagInfo> mobs = new TreeSet<FlagInfo>();
	private final Comparator<FlagInfo> priorityCompare = new Comparator<FlagInfo>() {
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
				return mobs.contains(StringUtils.format(CreatureTask
						.getCreature(entity).getType(), false));
			} else if (entity instanceof Player) {
				Player player = (Player) entity;
				if (players.contains(player.getName()))
					return true;
				else {
					List<Group> groups = Permission.getGroups(player);

				}
				return true;
			} else {
				return mobs.contains(EntityUtils.getMonsterName(entity));
			}
		}
	};

	public void process(List<LivingEntity> toProcess) {
	}
}
