package com.citizens.utils;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.citizens.SettingsManager.Constant;
import com.citizens.resources.npclib.HumanNPC;

public class PathUtils {
	public static boolean createPath(HumanNPC npc, Location loc, int pathTicks,
			int stationaryTicks, float range) {
		return npc.getHandle()
				.startPath(loc, pathTicks, stationaryTicks, range);
	}

	public static boolean createPath(HumanNPC npc, Location loc, int pathTicks,
			int stationaryTicks) {
		return createPath(npc, loc, pathTicks, stationaryTicks,
				Constant.PathfindingRange.getFloat());
	}

	public static boolean createPath(HumanNPC npc, Location loc, int pathTicks) {
		return createPath(npc, loc, pathTicks,
				Constant.MaxStationaryTicks.getInt());
	}

	public static boolean createPath(HumanNPC npc, Location loc) {
		return createPath(npc, loc, Constant.MaxPathingTicks.getInt());
	}

	public static void target(HumanNPC npc, LivingEntity entity, boolean aggro,
			int pathTicks, int stationaryTicks, float range) {
		npc.getHandle().setTarget(entity, aggro, pathTicks, stationaryTicks,
				range);
	}

	public static void target(HumanNPC npc, LivingEntity entity, boolean aggro,
			int pathTicks, int stationaryTicks) {
		target(npc, entity, aggro, pathTicks, stationaryTicks,
				Constant.PathfindingRange.getFloat());
	}

	public static void target(HumanNPC npc, LivingEntity entity, boolean aggro,
			int pathTicks) {
		target(npc, entity, aggro, pathTicks,
				Constant.MaxStationaryTicks.getInt());
	}

	public static void target(HumanNPC npc, LivingEntity entity, boolean aggro) {
		target(npc, entity, aggro, Constant.MaxPathingTicks.getInt());
	}

	public static boolean pathFinished(HumanNPC npc) {
		return npc.getHandle().pathFinished();
	}

	public static void cancelPath(HumanNPC npc) {
		npc.getHandle().cancelPath();
	}

	public static void cancelTarget(HumanNPC npc) {
		npc.getHandle().cancelTarget();
	}

	public static boolean hasTarget(HumanNPC npc) {
		return npc.getHandle().hasTarget();
	}

	public static void setAttackTimes(HumanNPC npc, int times) {
		npc.getHandle().setAttackTimes(times);
	}
}