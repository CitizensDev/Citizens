package net.citizensnpcs.utils;

import net.citizensnpcs.properties.SettingsManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class PathUtils {
	public static boolean createPath(HumanNPC npc, Location loc, int pathTicks,
			int stationaryTicks, double range) {
		return npc.getHandle()
				.startPath(loc, pathTicks, stationaryTicks, range);
	}

	public static boolean createPath(HumanNPC npc, Location loc, int pathTicks,
			int stationaryTicks) {
		return createPath(npc, loc, pathTicks, stationaryTicks,
				SettingsManager.getDouble("PathfindingRange"));
	}

	public static boolean createPath(HumanNPC npc, Location loc, int pathTicks) {
		return createPath(npc, loc, pathTicks,
				SettingsManager.getInt("MaxStationaryTicks"));
	}

	public static boolean createPath(HumanNPC npc, Location loc) {
		return createPath(npc, loc, SettingsManager.getInt("MaxPathingTicks"));
	}

	public static void target(HumanNPC npc, LivingEntity entity, boolean aggro,
			int pathTicks, int stationaryTicks, double range) {
		npc.getHandle().setTarget(entity, aggro, pathTicks, stationaryTicks,
				range);
	}

	public static void target(HumanNPC npc, LivingEntity entity, boolean aggro,
			int pathTicks, int stationaryTicks) {
		target(npc, entity, aggro, pathTicks, stationaryTicks,
				SettingsManager.getDouble("PathfindingRange"));
	}

	public static void target(HumanNPC npc, LivingEntity entity, boolean aggro,
			int pathTicks) {
		target(npc, entity, aggro, pathTicks,
				SettingsManager.getInt("MaxStationaryTicks"));
	}

	public static void target(HumanNPC npc, LivingEntity entity, boolean aggro) {
		target(npc, entity, aggro, SettingsManager.getInt("MaxPathingTicks"));
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